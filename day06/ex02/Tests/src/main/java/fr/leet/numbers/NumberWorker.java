package fr.leet.numbers;

public class NumberWorker {

	public static class IllegalNumberException extends Exception {
		public IllegalNumberException () {
			super("IllegalNumberException");
		}
	}

	public boolean isPrime(int number) throws IllegalNumberException {

		if (number <= 1) {
			throw new IllegalNumberException();
		}

		int d = 2;

		while (d <= Math.sqrt(number)) {
			if (number % d == 0) return false;	
			d++;
		}	
		return true;
	}

	public int digitsSum(int number) {
		int total = 0;

		while (number != 0) {
			total += number % 10;
			number /= 10;
		}	

		return total;
	}
}
