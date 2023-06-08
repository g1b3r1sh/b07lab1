import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Polynomial {
	public double[] coefficients;
	public int[] exponents;
	
	
	private void initialise(double[] coefficients, int[] exponents) {
		this.coefficients = coefficients.clone();
		this.exponents = exponents.clone();
		this.simplify();
	}	
	
	private void initialise(String poly_str) {
		// Since minus signs only occur before coefficients, add plus sign in front to allow for easier processing
		poly_str = poly_str.replace("-", "+-");
		
		// Parse string into coefficient and exponent arrays
		String[] terms = poly_str.split("\\+");
		double[] new_coefficients = new double[terms.length];
		int[] new_exponents = new int[terms.length];
		for (int i = 0; i < terms.length; i++) {
			String term = terms[i];
			double coefficient;
			int exponent;
			if (term.contains("x")) {
				String[] numbers = term.split("x");
				coefficient = Double.parseDouble(numbers[0]);
				exponent = Integer.parseInt(numbers[1]);
			}
			else {
				// Special case for zero degree term
				coefficient = Double.parseDouble(term);
				exponent = 0;
			}
			new_coefficients[i] = coefficient;
			new_exponents[i] = exponent;
		}
		
		this.initialise(new_coefficients, new_exponents);
	}
	
	public Polynomial(double[] coefficients, int[] exponents) {
		this.initialise(coefficients, exponents);
	}

	public Polynomial() {
		this(new double[0], new int[0]);
	}
	
	public Polynomial(String poly_str) {
		this.initialise(poly_str);
	}
	
	public Polynomial(File file) throws FileNotFoundException {
		// Read file data into string
		Scanner scanner = new Scanner(file);
		String poly_str = scanner.next();
		scanner.close();
		
		this.initialise(poly_str);
	}
	
	/**
	 * Simplifies polynomial representation by removing redundant information from arrays
	 */
	private void simplify() {
		this.combine_duplicate_exponents();
		this.remove_non_zero_coefficients();
	}
	
	/**
	 *  Removes coefficients that are zero
	 */
	private void remove_non_zero_coefficients() {
		// Calculate number of non zero coefficients so we know the length of the new arrays
		// At the same time, we shift the non zero coefficients to the left so that
		//     the first numNonZeroCoefficients elements in the arrays have non zero coefficients
		int num_non_zero_coefficients = 0;
		for (int i = 0; i < this.coefficients.length; i++) {
			// If zero coefficient detected, shift all future array values left
			if (this.coefficients[i] != 0) {
				this.coefficients[num_non_zero_coefficients] = this.coefficients[i];
				this.exponents[num_non_zero_coefficients] = this.exponents[i];
				num_non_zero_coefficients++;
			}
		}
		
		// Create new arrays and copy non zero coefficient values
		double[] new_coefficients = new double[num_non_zero_coefficients];
		int[] new_exponents = new int[num_non_zero_coefficients];
		for (int i = 0; i < num_non_zero_coefficients; i++) {
			new_coefficients[i] = this.coefficients[i];
			new_exponents[i] = this.exponents[i];
		}
		
		// Set current arrays to new arrays
		this.coefficients = new_coefficients;
		this.exponents = new_exponents;
	}
	
	/**
	 * Returns index of first instance of exponent in exponent array, or -1 if exponent not encountered
	 */
	private int find_exponent(int exponent) {
		for (int i = 0; i < this.exponents.length; i++) {
			if (this.exponents[i] == exponent) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Combines duplicate exponent values by adding their coefficients
	 */
	private void combine_duplicate_exponents() {
		// Calculate number of unique exponents so we know the length of the new arrays
		// At the same time, we shift the non zero coefficients to the left so that
		//     the first numNonZeroCoefficients elements in the arrays have non zero coefficients
		int num_unique_exponents = 0;
		for (int i = 0; i < this.exponents.length; i++) {
			int exponent_index = this.find_exponent(this.exponents[i]); 
			if (exponent_index == i) {
				// If duplicate exponent detected, shift all future array values left
				this.coefficients[num_unique_exponents] = this.coefficients[i];
				this.exponents[num_unique_exponents] = this.exponents[i];
				num_unique_exponents++;
			}
			else {
				// Otherwise, combine coefficients of duplicate value
				this.coefficients[exponent_index] += this.coefficients[i];
			}
		}
		
		// Create new arrays and copy unique exponent values
		double[] new_coefficients = new double[num_unique_exponents];
		int[] new_exponents = new int[num_unique_exponents];
		for (int i = 0; i < num_unique_exponents; i++) {
			new_coefficients[i] = this.coefficients[i];
			new_exponents[i] = this.exponents[i];
		}
		
		// Set current arrays to new arrays
		this.coefficients = new_coefficients;
		this.exponents = new_exponents;
	}
	
	public Polynomial add(Polynomial p) {
		// Just combine both arrays and take advantage of simplification in the constructor :)
		double[] new_coefficients = new double[this.coefficients.length + p.coefficients.length];
		int[] new_exponents = new int[this.exponents.length + p.exponents.length];
		for (int i = 0; i < this.coefficients.length; i++) {
			new_coefficients[i] = this.coefficients[i];
			new_exponents[i] = this.exponents[i];
		}
		for (int i = 0; i < p.coefficients.length; i++) {
			new_coefficients[this.coefficients.length + i] = p.coefficients[i];
			new_exponents[this.exponents.length + i] = p.exponents[i];
		}

		return new Polynomial(new_coefficients, new_exponents);
	}
	
	public Polynomial multiply(Polynomial p) {
		// Create arrays containing product between all entries in two polynomials,
		//     where [i + j * this.length] is index of product of ith entry in this and jth entry in p
		// Note that arrays will be simplified in constructor of new polynomial
		double[] new_coefficients = new double[this.coefficients.length * p.coefficients.length];
		int[] new_exponents = new int[this.exponents.length * p.exponents.length];
		for (int i = 0; i < this.coefficients.length; i++) {
			for (int j = 0; j < this.coefficients.length; j++) {
				new_coefficients[i + j * this.coefficients.length] = this.coefficients[i] * p.coefficients[j];
				new_exponents[i + j * this.exponents.length] = this.exponents[i] + p.exponents[j];
			}
		}
		
		return new Polynomial(new_coefficients, new_exponents);
	}
	
	public double evaluate(double x) {
		double sum = 0;
		for (int i = 0; i < this.coefficients.length; i++) {
			sum += this.coefficients[i] * Math.pow(x, this.exponents[i]);
		}
		return sum;
	}
	
	public boolean hasRoot(double x) {
		return this.evaluate(x) == 0;
	}
}
