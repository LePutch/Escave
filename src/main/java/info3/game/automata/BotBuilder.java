package info3.game.automata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import info3.game.automata.actions.Egg;
import info3.game.automata.actions.Explode;
import info3.game.automata.actions.Get;
import info3.game.automata.actions.IAction;
import info3.game.automata.actions.Jump;
import info3.game.automata.actions.Move;
import info3.game.automata.actions.Pop;
import info3.game.automata.actions.Protect;
import info3.game.automata.actions.Throw;
import info3.game.automata.actions.Wizz;
import info3.game.automata.ast.AST;
import info3.game.automata.ast.Action;
import info3.game.automata.ast.Automaton;
import info3.game.automata.ast.Behaviour;
import info3.game.automata.ast.BinaryOp;
import info3.game.automata.ast.Category;
import info3.game.automata.ast.Condition;
import info3.game.automata.ast.Direction;
import info3.game.automata.ast.FunCall;
import info3.game.automata.ast.IVisitor;
import info3.game.automata.ast.Key;
import info3.game.automata.ast.Mode;
import info3.game.automata.ast.State;
import info3.game.automata.ast.Transition;
import info3.game.automata.ast.UnaryOp;
import info3.game.automata.ast.Underscore;
import info3.game.automata.ast.Value;
import info3.game.automata.conditions.Cell;
import info3.game.automata.conditions.Closest;
import info3.game.automata.conditions.GotPower;
import info3.game.automata.conditions.GotStuff;
import info3.game.automata.conditions.ICondition;
import info3.game.automata.conditions.MyDir;
import info3.game.automata.conditions.Not;
import info3.game.automata.conditions.True;
import info3.game.automata.parser.AutomataParser;

public class BotBuilder implements IVisitor {

	ArrayList<Automata> automatas;
	Automata currentAutomata;

	public static void main(String[] args) throws Exception {
		AST ast;
		ast = new AutomataParser(new BufferedReader(new FileReader("src/main/resources/automatas.gal"))).Run();

		BotBuilder bot_builder = new BotBuilder();
		ast.accept(bot_builder);
		System.out.println(bot_builder.getAutomatas());
	}

	public ArrayList<Automata> getAutomatas() {
		return automatas;
	}

	public BotBuilder() {
		this.automatas = new ArrayList<Automata>();
	}

	@Override
	public Object visit(Category cat) {
		return this.astCategoryToinfo3Category(cat);
	}

	@Override
	public Object visit(Direction dir) {
		return this.astDirectionToinfo3Direction(dir);
	}

	@Override
	public Object visit(Key key) {
		if (key.terminal.content.length() != 1)
			return (int) (key.terminal.content.charAt(0)) * 10 + (int) key.terminal.content.charAt(1);
		return (int) key.terminal.content.charAt(0);
	}

	@Override
	public Object visit(Value v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Underscore u) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enter(FunCall funcall) {
	}

	public info3.game.automata.Direction astDirectionToinfo3Direction(Direction d) {
		if (d.terminal.content.equals("NW"))
			return info3.game.automata.Direction.NORTHWEST;
		else if (d.terminal.content.equals("NE"))
			return info3.game.automata.Direction.NORTHWEST;
		switch (d.terminal.content.charAt(0)) {
		case 'N':
			return info3.game.automata.Direction.NORTH;
		case 'S':
			return info3.game.automata.Direction.SOUTH;
		case 'W':
			return info3.game.automata.Direction.WEST;
		case 'E':
			return info3.game.automata.Direction.EST;
		case 'H':
			return info3.game.automata.Direction.HERE;
		}
		return null;
	}

	public info3.game.automata.Category astCategoryToinfo3Category(Category c) {
		switch (c.terminal.content.charAt(0)) {
		case 'A':
			return info3.game.automata.Category.ADVERSAIRE;
		case 'J':
			return info3.game.automata.Category.JUMPABLE;
		case 'O':
			return info3.game.automata.Category.OBJECT;
		case '@':
			return info3.game.automata.Category.PLAYER;
		case 'T':
			return info3.game.automata.Category.TEAM;
		case '_':
			return info3.game.automata.Category.SOMETHING;
		case 'V':
			return info3.game.automata.Category.VOID;
		case 'C':
			return info3.game.automata.Category.C;
		case 'D':
			return info3.game.automata.Category.D;
		case 'G':
			return info3.game.automata.Category.G;
		case 'M':
			return info3.game.automata.Category.M;
		case 'X':
			return info3.game.automata.Category.X;
		}
		return null;
	}

	@Override
	public Object exit(FunCall funcall, List<Object> parameters) {
		switch (funcall.name) {
		case "Cell":
			return new Cell((info3.game.automata.Direction) parameters.get(0),
					(info3.game.automata.Category) parameters.get(1));
		case "Closest":
			return new Closest((info3.game.automata.Category) parameters.get(0),
					(info3.game.automata.Direction) parameters.get(1));
		case "GotPower":
			return new GotPower();
		case "GotStuff":
			return new GotStuff();
		case "Key":
			return new info3.game.automata.conditions.Key((int) parameters.get(0));
		case "MyDir":
			return new MyDir((info3.game.automata.Direction) parameters.get(0));
		case "True":
			return new True();
		case "Egg":
			return new Egg((info3.game.automata.Direction) parameters.get(0));
		case "Explode":
			return new Explode();
		case "Get":
			return new Get();
		case "Jump":
			return new Jump();
		case "Move":
			return new Move((info3.game.automata.Direction) parameters.get(0));
		case "Pop":
			if (parameters.size() == 0) {
				return new Pop(null);
			}
			return new Pop((info3.game.automata.Direction) parameters.get(0));
		case "Protect":
			return new Protect((info3.game.automata.Direction) parameters.get(0));
		case "Throw":
			return new Throw();
		case "Wizz":
			if (parameters.size() == 0) {
				return new Wizz(null);
			}
			return new Wizz((info3.game.automata.Direction) parameters.get(0));
		default:
			System.out.println("[WARNING] Unexpected condition or action: " + funcall.name);
			return null;
		}
	}

	@Override
	public Object visit(BinaryOp operator, Object left, Object right) {
		if (left instanceof IAction) {
			ArrayList<IAction> actions = new ArrayList<IAction>();
			actions.add((IAction) left);
			actions.add((IAction) right);
			return actions;
		} else if (left instanceof ICondition) {
			ArrayList<ICondition> conditions = new ArrayList<ICondition>();
			conditions.add((ICondition) left);
			conditions.add((ICondition) right);
			return conditions;
		}
		System.out.println("[WARNING] Visit BinaryOp");
		return null;
	}

	@Override
	public Object visit(UnaryOp operator, Object expression) {
		if (expression instanceof ICondition) {
			ArrayList<ICondition> conditions = new ArrayList<ICondition>();
			conditions.add(new Not((ICondition) expression));
			return conditions;
		}
		return null;
	}

	@Override
	public Object visit(State state) {
		AutomataState newState = new AutomataState(state.name);
		newState = this.currentAutomata.addState(newState);
		return newState;
	}

	@Override
	public void enter(Mode mode) {
		return;
	}

	@Override
	public Object exit(Mode mode, Object source_state, Object behaviour) {
		for (Object o : (List<Object>) behaviour) {
			AutomataTransition transition = (AutomataTransition) o;
			transition.setSourceState((AutomataState) source_state);
			((AutomataState) source_state).addTransition(transition);
		}
		return null;
	}

	@Override
	public Object visit(Behaviour behaviour, List<Object> transitions) {
		return transitions;
	}

	@Override
	public void enter(Condition condition) {
		return;
	}

	@Override
	public Object exit(Condition condition, Object expression) {
		if (expression instanceof ICondition) {
			ArrayList<ICondition> conditions = new ArrayList<ICondition>();
			conditions.add((ICondition) expression);
			return conditions;
		}
		return expression;
	}

	@Override
	public void enter(Action acton) {
		return;
	}

	@Override
	public Object exit(Action action, List<Object> funcalls) {
		// this.actions.add((IAction) funcalls.get(0));
		ArrayList<IAction> actions = new ArrayList<IAction>();
		for (Object o : funcalls) {
			actions.add((IAction) o);
		}
		return actions;
	}

	@Override
	public Object visit(Transition transition, Object condition, Object action, Object target_state) {
		AutomataTransition newTransition = new AutomataTransition((ArrayList<ICondition>) condition,
				(ArrayList<IAction>) action, (AutomataState) target_state);
		return newTransition;
	}

	@Override
	public void enter(Automaton automaton) {
		this.currentAutomata = new Automata(automaton.name);
		this.automatas.add(currentAutomata);
	}

	@Override
	public Object exit(Automaton automaton, Object initial_state, List<Object> modes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(AST bot, List<Object> automata) {
		// TODO Auto-generated method stub
		return null;
	}

}
