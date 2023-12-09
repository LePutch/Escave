package info3.game.automata;

import java.util.ArrayList;

import info3.game.automata.actions.IAction;
import info3.game.automata.conditions.ICondition;

public class AutomataTransition {
	AutomataState sourceState;
	ArrayList<ICondition> conditions;
	ArrayList<IAction> actions;
	AutomataState targetState;

	public AutomataTransition(ArrayList<ICondition> conditions, ArrayList<IAction> actions, AutomataState targetState) {
		this.sourceState = null;
		this.conditions = (ArrayList<ICondition>) conditions.clone();
		this.actions = (ArrayList<IAction>) actions.clone();
		this.targetState = targetState;
	}

	public void setSourceState(AutomataState state) {
		this.sourceState = state;
	}

	public AutomataState getSourceState() {
		return sourceState;
	}

	public ArrayList<ICondition> getConditions() {
		return conditions;
	}

	public ArrayList<IAction> getActions() {
		return actions;
	}

	public AutomataState getTargetState() {
		return targetState;
	}

	public String toString() {
		String out = this.getSourceState() + ": ";
		out += this.getConditions().get(0);
		for (int i = 1; i < this.getConditions().size(); i++) {
			out += " & " + this.getConditions().get(i);
		}
		if (this.getActions().size() != 0) {
			out += " ? " + this.getActions().get(0);
			for (int i = 1; i < this.getActions().size(); i++) {
				out += " & " + this.getActions().get(i);
			}
		}
		out += " :" + this.getTargetState();
		return out;
	}
}
