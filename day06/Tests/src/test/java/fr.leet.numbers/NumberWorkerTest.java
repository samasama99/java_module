import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import fr.leet.numbers.NumberWorker;

import static org.junit.jupiter.api.Assertions.*;

class NumberWorkerTest {
	@ParameterizedTest
	@ValueSource(ints = {3, 5, 7})
	void isPrimeForPrimes(int number) {
		NumberWorker numberWorker = new NumberWorker();
		assertDoesNotThrow(() -> { assertTrue(numberWorker.isPrime(number)); });
	}

	@ParameterizedTest
	@ValueSource(ints = {4, 6, 9})
	void isPrimeForNotPrimes(int number) {
		NumberWorker numberWorker = new NumberWorker();
		assertDoesNotThrow(() -> { assertFalse(numberWorker.isPrime(number)); });
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 1, -111})
	void isPrimeForNoneValidNumbers(int number) {
		NumberWorker numberWorker = new NumberWorker();
		assertThrows(NumberWorker.IllegalNumberException.class, () -> { numberWorker.isPrime(number); });
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/data.csv")
	void digitsSumTest(String input, String expected) {
		NumberWorker numberWorker = new NumberWorker();
		int number = Integer.parseInt(input);
		int sum = Integer.parseInt(expected);
		assertEquals(sum, numberWorker.digitsSum(number));
	}
}
