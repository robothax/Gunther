import java.util.Scanner;

import data_structures.Sequent;
import data_structures.Term;
import parser.Parser;
import prover.Prover;


public class TheoremProver {

	public static void main(String[] args) {
		System.out.print("Query:/> ");

		Scanner input = new Scanner(System.in);
		String query = input.nextLine();

		Sequent q = Parser.parse(query);
		
		System.out.println("Statement to prove");
		System.out.println(q.toString() + "\n");
		
		Term.initMetavariables();
		Prover.initProof(q);

		while(!Prover.isComplete());

		Prover.getProof().printProof();
	}
}
