import java.util.Scanner;

import data_structures.Sequent;
import parser.Parser;
import prover.Prover;


public class TheoremProver {

	public static void main(String[] args) {
		System.out.println("Query:/>");

		Scanner input = new Scanner(System.in);
		String query = input.nextLine();

		Sequent q = Parser.parse(query);
		
		System.out.println(q.toString());
		Prover.initProof(q);

		while(!Prover.isComplete());

		Prover.getProof().printProof();
	}
}
