package info3.game.automata;

public class CurrentState {
	AutomataState state;
	boolean blocked;
	long remaningMs;

	public CurrentState(AutomataState s) {
		this.state = s;
		this.blocked = false;
		this.remaningMs = 0;
//		if (this.getState().getName().equals("Cooldown")) {
//			this.block(2000);
//		}
		if (this.getState().getName().startsWith("Cooldown")) {
			this.block(Integer.valueOf(this.getState().getName().substring(8)));
		}
	}

	/**
	 * Permet de bloquer l'état courant
	 * 
	 * @param ticks Le nombre de ticks avant de débloquer l'état
	 */
	public void block(long ms) {
		this.blocked = true;
		this.remaningMs = ms;
	}

	public void setState(AutomataState state) {
		this.state = state;
	}

	public AutomataState getState() {
		return this.state;
	}

	public void step(long elapsed) {
		this.remaningMs -= elapsed;
		if (this.remaningMs <= 0) {
			this.remaningMs = 0;
			this.blocked = false;
		}
	}

	public boolean blocked() {
		return this.blocked;
	}
}
