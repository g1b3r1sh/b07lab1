class Polynomial {
	public double[] coefficients;
	public int[] exponents;

	public Polynomial(double[] coefficients, int[] exponents) {
		this.coefficients = coefficients.clone();
		this.exponents = exponents.clone();
		this.simplify();
	}

	public Polynomial() {
		this(new double[0], new int[0]);
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
}
