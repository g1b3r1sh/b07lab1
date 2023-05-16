class Polynomial {
	public double[] coefficients;

	public Polynomial(double[] coefficients) {
		this.coefficients = coefficients.clone();
	}

	public Polynomial() {
		this(new double[]{0});
	}

	public Polynomial add(Polynomial p) {
		// Get larger coefficient array
		double[] smaller_coeffs = this.coefficients.length < p.coefficients.length ? this.coefficients : p.coefficients;
		double[] larger_coeffs = this.coefficients.length < p.coefficients.length ? p.coefficients : this.coefficients;

		// Initialise new coefficient array as larger coefficient array
		double[] new_coeffs = larger_coeffs.clone();

		// Add elements of smaller array to larger array
		for (int i = 0; i < smaller_coeffs.length; i++) {
			new_coeffs[i] += smaller_coeffs[i];
		}

		return new Polynomial(new_coeffs);
	}

	public double evaluate(double x) {
		double sum = 0;
		for (int i = 0; i < this.coefficients.length; i++) {
			sum += this.coefficients[i] * Math.pow(x, i);
		}
		return sum;
	}

	public boolean hasRoot(double x) {
		return this.evaluate(x) == 0;
	}
}
