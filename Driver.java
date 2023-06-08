import java.io.File;
import java.io.IOException;

public class Driver {
	public static void main(String[] args) throws IOException {
		Polynomial p = new Polynomial();
		System.out.println(p.evaluate(3));
		double[] c1 = {6, 0, 0, 5};
		int[] e1 = {0, 1, 2, 3};
		Polynomial p1 = new Polynomial(c1, e1);
		double[] c2 = {0, -2, 0, 0, -9};
		int[] e2 = {0, 1, 2, 3, 4};
		Polynomial p2 = new Polynomial(c2, e2);
		Polynomial s = p1.add(p2);
		System.out.println("s(0.1)=" + s.evaluate(0.1));
		if (s.hasRoot(1))
			System.out.println("1 is a root of s");
		else
			System.out.println("1 is not a root of s");
		Polynomial p12 = p1.multiply(p2);
		for (int i = 0; i < 10; i++) {
			System.out.println("p1(" + i + ")*p2(" + i + ") = " + p12.evaluate(i) + " = " + p1.evaluate(i) * p2.evaluate(i));
		}
		System.out.println("p1*p2 " + (has_duplicates(p12) ? "has" : "does not have") + " duplicates");
		Polynomial p3 = new Polynomial("5-3x2+7x8");
		System.out.println("p3(1) = (" + p3 + ")(1) = " + p3.evaluate(1));
		System.out.println("Saving p3 to test.txt");
		p3.saveToFile("test.txt");
		Polynomial p4 = new Polynomial(new File("test.txt"));
		System.out.println(p3 + " = " + p4);
	}
	
	public static boolean has_duplicates(Polynomial p) {
		for (int i = 0; i < p.exponents.length; i++) {
			for (int j = 0; j < i; j++) {
				if (p.exponents[i] == p.exponents[j]) {
					return true;
				}
			}
		}
		return false;
	}
}
