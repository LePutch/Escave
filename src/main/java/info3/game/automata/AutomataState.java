package info3.game.automata;

import java.util.ArrayList;

import info3.game.automata.actions.IAction;
import info3.game.automata.conditions.ICondition;
import info3.game.entities.Entity;

public class AutomataState {
	String name;
	ArrayList<AutomataTransition> transitions;

	public AutomataState(String name) {
		this.name = name;
		this.transitions = new ArrayList<AutomataTransition>();
	}

	public AutomataState(String name, ArrayList<AutomataTransition> transitions) {
		this.name = name;
		this.transitions = transitions;
	}

	public void addTransition(AutomataTransition t) {
		this.transitions.add(t);
	}

	public ArrayList<AutomataTransition> getTransitions() {
		return this.transitions;
	}

	public AutomataState step(Entity e) {
		// TODO Vérifier si une transition est faisable et retourner le nouvel état
		for (AutomataTransition transition : this.getTransitions()) {
			boolean valid = true;
			for (ICondition condition : transition.getConditions()) {
				if (!condition.eval(e))
					valid = false;
			}
			if (valid) {
				for (IAction action : transition.getActions()) {
					action.apply(e);
				}
				return transition.getTargetState();
			}
		}
		return null;
	}

	public String getName() {
		return this.name;
	}

	public String toString() {
		return "(" + this.getName() + ")";
	}
}
