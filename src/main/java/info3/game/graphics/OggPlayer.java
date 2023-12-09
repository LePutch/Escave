/*
 * Derivative work from http://www.jcraft.com/jorbis/tutorial/Tutorial.html
 * 
 * ==========================================================================
 * Original Copyright/License
 * ==========================================================================
 * 
 * Copyright &copy; Jon Kristensen, 2008.
 * All rights reserved.
 * 
 * This is version 1.0 of this source code, made to work with JOrbis 1.x. The
 * last time this file was updated was the 15th of March, 2008.
 * 
 * Version history:
 * 
 * 1.0: Initial release.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 * 
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *   * Neither the name of jonkri.com nor the names of its contributors may be
 *     used to endorse or promote products derived from this software without
 *     specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package info3.game.graphics;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;

import info3.game.sound.AudioPlayer;
import info3.game.sound.AudioPlayerListener;

public class OggPlayer extends AudioPlayer implements Runnable {

	private String name;
	private float volume;
	private Thread worker;

	// If you wish to debug this source, please set the variable below to true.
	private final boolean debugMode = false;
	private final static boolean PLAY_VERBOSE = false;
	private final static boolean VOLUME_VERBOSE = false;

	/*
	 * audio stream, ogg-vorbis format.
	 */
	private InputStream inputStream;

	/*
	 * We need a buffer, it's size, a count to know how many bytes we have read and
	 * an index to keep track of where we are. This is standard networking stuff
	 * used with read().
	 */
	byte[] buffer;
	int bufferSize;
	int count;
	int length;
	int index;

	/*
	 * JOgg and JOrbis require fields for the converted buffer. This is a buffer
	 * that is modified in regards to the number of audio channels. Naturally, it
	 * will also need a size.
	 */
	byte[] convertedBuffer;
	int convertedBufferSize;

	// The source data line onto which data can be written.
	private SourceDataLine outputLine;

	// A three-dimensional an array with PCM information.
	private float[][][] pcmInfo;

	// The index for the PCM information.
	private int[] pcmIndex;

	// Here are the four required JOgg objects...
	private Packet joggPacket;
	private Page joggPage;
	private StreamState joggStreamState;
	private SyncState joggSyncState;

	// ... followed by the four required JOrbis objects.
	private DspState jorbisDspState;
	private Block jorbisBlock;
	private Comment jorbisComment;
	private Info jorbisInfo;

	private volatile boolean stop;
	private long duration;
	private long startOfPlay = -1L;
	private long endOfPlay = -1L;
	private boolean loop;

	OggPlayer(GameCanvas canvas) {
		super(canvas);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void stop() {
		stop = true;
		worker.interrupt();
	}

	@Override
	public void playMusic(String name, InputStream is, long duration, float volume) {
		this.name = name;
		this.volume = volume;
		inputStream = is;
		this.duration = duration;
		loop = true;
		is.mark(Integer.MAX_VALUE);
		if (worker != null) {
			stop = true;
			worker.interrupt();
		} else {
			worker = new Thread(this, "AudioPlayer:" + name);
			worker.start();
		}
		// _play(name, is, duration, volume);
	}

	@Override
	public void playSound(String name, InputStream is, long duration, float volume, AudioPlayerListener l) {
		this.name = name;
		this.volume = volume;
		inputStream = is;
		this.duration = duration;
		listener = l;
		if (worker != null) {
			stop = true;
			worker.interrupt();
		} else {
			worker = new Thread(this, "AudioPlayer:" + name);
			worker.start();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				if (PLAY_VERBOSE)
					System.out.println("\nPlaying: " + name);
				inputStream.reset();
				startOfPlay = System.currentTimeMillis();
				if (duration > 0)
					endOfPlay = startOfPlay + duration;
				initializeJOrbis();
				if (readHeader()) {
					if (initializeSound()) {
						debugOutput("Reading the body. length=" + length);
						readBody();
						debugOutput("Done reading the body. length=" + length);
					} else
						System.out.println("Failed initializing sound");
				} else
					System.out.println("Failed reading header");
				if (!loop || stop)
					break;
				cleanUp();
				inputStream.reset();
			}
		} catch (IOException ex) {
			debugOutput("Got exception=" + ex);
		} finally {
			cleanUp();
		}
		try {
			if (PLAY_VERBOSE) {
				long end = endOfPlay;
				if (end < 0)
					end = System.currentTimeMillis();
				System.out.println("Done[" + name + "]: elapsed=" + (end - startOfPlay) + " length=" + length);
			}
			if (listener != null)
				listener.endOfPlay(this, name);
		} catch (Throwable th) {
		}
		// Closes the stream.
		try {
			if (inputStream != null)
				inputStream.close();
		} catch (Exception e) {
		}
		canvas.stopped(this);
	}

	/**
	 * Initializes JOrbis. First, we initialize the <code>SyncState</code> object.
	 * After that, we prepare the <code>SyncState</code> buffer. Then we
	 * "initialize" our buffer, taking the data in <code>SyncState</code>.
	 */
	static Object lock = new Object();

	private void initializeJOrbis() {
		debugOutput("Initializing JOrbis.");
		synchronized (lock) {
			joggPacket = new Packet();
			joggPage = new Page();
			joggStreamState = new StreamState();
			joggSyncState = new SyncState();

			// ... followed by the four required JOrbis objects.
			jorbisDspState = new DspState();
			jorbisBlock = new Block(jorbisDspState);
			jorbisComment = new Comment();
			jorbisInfo = new Info();

			// Initialize SyncState
			joggSyncState.init();

			// Prepare the to SyncState internal buffer
			index = 0;
			bufferSize = 2048;
			joggSyncState.buffer(bufferSize);

			/*
			 * Fill the buffer with the data from SyncState's internal buffer. Note how the
			 * size of this new buffer is different from bufferSize.
			 */
			buffer = joggSyncState.data;
		}
		debugOutput("Done initializing JOrbis.");
	}

	/**
	 * This method reads the header of the stream, which consists of three packets.
	 * 
	 * @return true if the header was successfully read, false otherwise
	 */
	private boolean readHeader() throws IOException {
		debugOutput("Starting to read the header.");

		/*
		 * Variable used in loops below. While we need more data, we will continue to
		 * read from the InputStream.
		 */
		boolean needMoreData = true;

		/*
		 * We will read the first three packets of the header. We start off by defining
		 * packet = 1 and increment that value whenever we have successfully read
		 * another packet.
		 */
		int packet = 1;

		/*
		 * While we need more data (which we do until we have read the three header
		 * packets), this loop reads from the stream and has a big <code>switch</code>
		 * statement which does what it's supposed to do in regards to the current
		 * packet.
		 */
		index = 0;
		bufferSize = 2048;

		while (needMoreData) {
			// Read from the InputStream.
			count = inputStream.read(buffer, index, bufferSize);
			if (count == -1) {
				debugOutput(" end of stream");
				return false;
			}
			length += count;
			debugOutput(" read " + count + " bytes, index=" + index);
			// We let SyncState know how many bytes we read.
			if (joggSyncState.wrote(count) < 0) {
				debugError("SyncState wrote error", null);
				return false;
			}
			/*
			 * We want to read the first three packets. For the first packet, we need to
			 * initialize the StreamState object and a couple of other things. For packet
			 * two and three, the procedure is the same: we take out a page, and then we
			 * take out the packet.
			 */
			switch (packet) {
			// The first packet.
			case 1: {
				// We take out a page.
				switch (joggSyncState.pageout(joggPage)) {
				// If there is a hole in the data, we must exit.
				case -1: {
					debugOutput("There is a hole in the first " + "packet data.");
					return false;
				}

				// If we need more data, we break to get it.
				case 0: {
					break;
				}

				/*
				 * We got where we wanted. We have successfully read the first packet, and we
				 * will now initialize and reset StreamState, and initialize the Info and
				 * Comment objects. Afterwards we will check that the page doesn't contain any
				 * errors, that the packet doesn't contain any errors and that it's Vorbis data.
				 */
				case 1: {
					// Initializes and resets StreamState.
					joggStreamState.init(joggPage.serialno());
					joggStreamState.reset();

					// Initializes the Info and Comment objects.
					jorbisInfo.init();
					jorbisComment.init();

					// Check the page (serial number and stuff).
					if (joggStreamState.pagein(joggPage) == -1) {
						debugError("We got an error while " + "reading the first header page.", null);
						return false;
					}

					/*
					 * Try to extract a packet. All other return values than "1" indicates there's
					 * something wrong.
					 */
					if (joggStreamState.packetout(joggPacket) != 1) {
						debugError("We got an error while " + "reading the first header packet.", null);
						return false;
					}

					/*
					 * We give the packet to the Info object, so that it can extract the
					 * Comment-related information, among other things. If this fails, it's not
					 * Vorbis data.
					 */
					if (jorbisInfo.synthesis_headerin(jorbisComment, joggPacket) < 0) {
						debugError("We got an error while " + "interpreting the first packet. "
								+ "Apparantly, it's not Vorbis data.", null);
						return false;
					}

					// We're done here, let's increment "packet".
					packet++;
					break;
				}
				}

				/*
				 * Note how we are NOT breaking here if we have proceeded to the second packet.
				 * We don't want to read from the input stream again if it's not necessary.
				 */
				if (packet == 1)
					break;
			}

			// The code for the second and third packets follow.
			case 2:
			case 3: {
				// Try to get a new page again.
				switch (joggSyncState.pageout(joggPage)) {
				// If there is a hole in the data, we must exit.
				case -1: {
					debugError("There is a hole in the second " + "or third packet data.", null);
					return false;
				}

				// If we need more data, we break to get it.
				case 0: {
					break;
				}

				/*
				 * Here is where we take the page, extract a packet and and (if everything goes
				 * well) give the information to the Info and Comment objects like we did above.
				 */
				case 1: {
					// Share the page with the StreamState object.
					joggStreamState.pagein(joggPage);

					/*
					 * Just like the switch(...packetout...) lines above.
					 */
					switch (joggStreamState.packetout(joggPacket)) {
					// If there is a hole in the data, we must exit.
					case -1: {
						debugError("There is a hole in the first" + "packet data.", null);
						return false;
					}

					// If we need more data, we break to get it.
					case 0: {
						break;
					}

					// We got a packet, let's process it.
					case 1: {
						/*
						 * Like above, we give the packet to the Info and Comment objects.
						 */
						jorbisInfo.synthesis_headerin(jorbisComment, joggPacket);

						// Increment packet.
						packet++;

						if (packet == 4) {
							/*
							 * There is no fourth packet, so we will just end the loop here.
							 */
							needMoreData = false;
						}

						break;
					}
					}

					break;
				}
				}

				break;
			}
			}

			// We get the new index and an updated buffer.
			index = joggSyncState.buffer(bufferSize);
			buffer = joggSyncState.data;

			/*
			 * If we need more data but can't get it, the stream doesn't contain enough
			 * information.
			 */
			if (count == 0 && needMoreData) {
				debugOutput("Not enough header data was supplied.");
				return false;
			}
		}

		debugOutput("Finished reading the header. index=" + index);

		return true;
	}

	/**
	 * This method starts the sound system. It starts with initializing the
	 * <code>DspState</code> object, after which it sets up the <code>Block</code>
	 * object. Last but not least, it opens a line to the source data line.
	 * 
	 * @return true if the sound system was successfully started, false otherwise
	 */
	private boolean initializeSound() {
		debugOutput("Initializing the sound system.");
		synchronized (lock) {

			// This buffer is used by the decoding method.
			convertedBufferSize = bufferSize * 2;
			convertedBuffer = new byte[convertedBufferSize];

			// Initializes the DSP synthesis.
			jorbisDspState.synthesis_init(jorbisInfo);

			// Make the Block object aware of the DSP.
			jorbisBlock.init(jorbisDspState);

			// Wee need to know the channels and rate.
			int channels = jorbisInfo.channels;
			int rate = jorbisInfo.rate;

			// Creates an AudioFormat object and a DataLine.Info object.
			AudioFormat audioFormat = new AudioFormat((float) rate, 16, channels, true, false);
			DataLine.Info datalineInfo = new DataLine.Info(SourceDataLine.class, audioFormat,
					AudioSystem.NOT_SPECIFIED);

			// Check if the line is supported.
			if (!AudioSystem.isLineSupported(datalineInfo)) {
				System.err.println("Audio output line is not supported.");
				return false;
			}

			/*
			 * Everything seems to be alright. Let's try to open a line with the specified
			 * format and start the source data line.
			 */
			try {
				outputLine = (SourceDataLine) AudioSystem.getLine(datalineInfo);
				outputLine.open(audioFormat);

				FloatControl volumeControl = (FloatControl) outputLine.getControl(FloatControl.Type.MASTER_GAIN);
				float vol = volumeControl.getValue();
				float min = volumeControl.getMinimum();
				float max = volumeControl.getMaximum();
				vol = min + volume * (max - min);
				if (VOLUME_VERBOSE) {
					System.out.println("  volume min=" + min + " max=" + max);
					System.out.println("  setting volume=" + vol);
				}
				volumeControl.setValue(vol);

			} catch (LineUnavailableException exception) {
				System.err.println("The audio output line could not be opened due " + "to resource restrictions.");
				System.err.println(exception);
				return false;
			} catch (IllegalStateException exception) {
				System.err.println("The audio output line is already open.");
				System.err.println(exception);
				return false;
			} catch (SecurityException exception) {
				System.err.println("The audio output line could not be opened due " + "to security restrictions.");
				System.err.println(exception);
				return false;
			} catch (Throwable th) {
				System.err.println("The audio initialization failed.");
				System.err.println(th);
				return false;
			}

			// Start it.
			outputLine.start();

			/*
			 * We create the PCM variables. The index is an array with the same length as
			 * the number of audio channels.
			 */
			pcmInfo = new float[1][][];
			pcmIndex = new int[jorbisInfo.channels];
		}
		debugOutput("Done initializing the sound system.");

		return true;
	}

	/**
	 * This method reads the entire stream body. Whenever it extracts a packet, it
	 * will decode it by calling <code>decodeCurrentPacket()</code>.
	 */
	private void readBody() throws IOException {

		/*
		 * Variable used in loops below, like in readHeader(). While we need more data,
		 * we will continue to read from the InputStream.
		 */
		boolean needMoreData = true;

		while (needMoreData) {
			if (stop)
				return; // we are asked to stop
			if (endOfPlay != -1) {
				long now = System.currentTimeMillis();
				if (endOfPlay <= now) {
					System.out.println("Reached duration=" + duration);
					return;
				}
			}

			switch (joggSyncState.pageout(joggPage)) {
			// If there is a hole in the data, we just proceed.
			case -1: {
				debugOutput("There is a hole in the data. We proceed.");
			}

			// If we need more data, we break to get it.
			case 0: {
				break;
			}

			// If we have successfully checked out a page, we continue.
			case 1: {
				// Give the page to the StreamState object.
				joggStreamState.pagein(joggPage);

				// If granulepos() returns "0", we don't need more data.
				if (joggPage.granulepos() == 0) {
					needMoreData = false;
					break;
				}

				// Here is where we process the packets.
				processPackets: while (true) {
					if (stop)
						return; // we are asked to stop
					if (endOfPlay != -1) {
						long now = System.currentTimeMillis();
						if (endOfPlay <= now)
							return;
					}
					switch (joggStreamState.packetout(joggPacket)) {
					// Is it a hole in the data?
					case -1: {
						debugOutput("There is a hole in the data, we " + "continue though.");
					}

					// If we need more data, we break to get it.
					case 0: {
						break processPackets;
					}

					/*
					 * If we have the data we need, we decode the packet.
					 */
					case 1: {
						decodeCurrentPacket();
					}
					}
				}

				/*
				 * If the page is the end-of-stream, we don't need more data.
				 */
				if (joggPage.eos() != 0)
					needMoreData = false;
			}
			}

			// If we need more data...
			if (needMoreData) {
				// We get the new index and an updated buffer.
				index = joggSyncState.buffer(bufferSize);
				if (index < 0) {
					// end of stream
					debugOutput(" end of stream index=" + index);
					return;
				}
				buffer = joggSyncState.data;
				// Read from the InputStream.
				count = inputStream.read(buffer, index, bufferSize);
				if (count == -1) {
					debugOutput(" end of stream");
					return;
				}
				debugOutput(" read " + count + " bytes, index=" + index);

				// We let SyncState know how many bytes we read.
				length += count;
				if (joggSyncState.wrote(count) < 0) {
					debugError("SyncState wrote error", null);
					return;
				}
				// There's no more data in the stream.
				if (count == 0)
					needMoreData = false;
			}
		}
	}

	/**
	 * A clean-up method, called when everything is finished. Clears the JOgg/JOrbis
	 * objects and closes the <code>InputStream</code>.
	 */
	private void cleanUp() {
		debugOutput("Cleaning up.");
		synchronized (lock) {

			if (outputLine != null) {
				outputLine.drain();
				outputLine.stop();
				outputLine.flush();
				outputLine.close();
				outputLine = null;
			}

			// Clear the necessary JOgg/JOrbis objects.
			joggStreamState.clear();
			jorbisBlock.clear();
			jorbisDspState.clear();
			jorbisInfo.clear();
			joggSyncState.clear();

			stop = false;
			startOfPlay = -1L;
			endOfPlay = -1L;

			index = 0;

			debugOutput("Done cleaning up.");
		}
	}

	/**
	 * Decodes the current packet and sends it to the audio output line.
	 */
	private void decodeCurrentPacket() {
		int samples;

		// Check that the packet is a audio data packet etc.
		if (jorbisBlock.synthesis(joggPacket) == 0) {
			// Give the block to the DspState object.
			jorbisDspState.synthesis_blockin(jorbisBlock);
		}

		// We need to know how many samples to process.
		int range;

		/*
		 * Get the PCM information and count the samples. And while these samples are
		 * more than zero...
		 */
		while ((samples = jorbisDspState.synthesis_pcmout(pcmInfo, pcmIndex)) > 0) {
			// We need to know for how many samples we are going to process.
			if (samples < convertedBufferSize) {
				range = samples;
			} else {
				range = convertedBufferSize;
			}

			// For each channel...
			for (int i = 0; i < jorbisInfo.channels; i++) {
				int sampleIndex = i * 2;

				// For every sample in our range...
				for (int j = 0; j < range; j++) {
					/*
					 * Get the PCM value for the channel at the correct position.
					 */
					int value = (int) (pcmInfo[0][i][pcmIndex[i] + j] * 32767);

					/*
					 * We make sure our value doesn't exceed or falls below +-32767.
					 */
					if (value > 32767) {
						value = 32767;
					}
					if (value < -32768) {
						value = -32768;
					}

					/*
					 * It the value is less than zero, we bitwise-or it with 32768 (which is
					 * 1000000000000000 = 10^15).
					 */
					if (value < 0)
						value = value | 32768;

					/*
					 * Take our value and split it into two, one with the last byte and one with the
					 * first byte.
					 */
					convertedBuffer[sampleIndex] = (byte) (value);
					convertedBuffer[sampleIndex + 1] = (byte) (value >>> 8);

					/*
					 * Move the sample index forward by two (since that's how many values we get at
					 * once) times the number of channels.
					 */
					sampleIndex += 2 * (jorbisInfo.channels);
				}
			}

			// Write the buffer to the audio output line.
			outputLine.write(convertedBuffer, 0, 2 * jorbisInfo.channels * range);

			// Update the DspState object.
			jorbisDspState.synthesis_read(range);
		}
	}

	/**
	 * This method is being called internally to output debug information whenever
	 * that is wanted.
	 * 
	 * @param output the debug output information
	 */
	private void debugOutput(String output) {
		if (debugMode)
			System.out.println("OggPlayer: " + output);
	}

	private void debugError(String output, Throwable reason) {
		if (reason != null)
			reason.printStackTrace(System.err);
		System.err.println("OggPlayer: " + output);
	}

}
