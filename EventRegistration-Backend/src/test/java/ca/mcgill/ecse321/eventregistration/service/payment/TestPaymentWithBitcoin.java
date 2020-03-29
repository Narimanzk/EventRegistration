package ca.mcgill.ecse321.eventregistration.service.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.eventregistration.dao.BitcoinRepository;
import ca.mcgill.ecse321.eventregistration.dao.EventRepository;
import ca.mcgill.ecse321.eventregistration.dao.PersonRepository;
import ca.mcgill.ecse321.eventregistration.dao.RegistrationRepository;
import ca.mcgill.ecse321.eventregistration.model.Bitcoin;
import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Person;
import ca.mcgill.ecse321.eventregistration.model.Registration;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPaymentWithBitcoin {
	@Autowired
	private EventRegistrationService service;

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private RegistrationRepository registrationRepository;
	@Autowired
	private BitcoinRepository bitcoinRepository;

	@After
	public void clearDatabase() {
		// Fisrt, we clear registrations to avoid exceptions due to inconsistencies
		registrationRepository.deleteAll();
		// Then we can clear the other tables
		personRepository.deleteAll();
		eventRepository.deleteAll();
		bitcoinRepository.deleteAll();
	}

	@Test
	public void test_01_testPayWithBitcoin() {
		try {
			Person person = TestUtils.setupPerson(service, TestPaymentWithBitcoinData.TEST01_PERSON_NAME);
			Event event = TestUtils.setupEvent(service, TestPaymentWithBitcoinData.TEST01_EVENT_NAME);
			Registration r = TestUtils.register(service, person, event);
			Bitcoin ap = service.createBitcoinPay(TestPaymentWithBitcoinData.TEST01_VALID_ID,
					TestPaymentWithBitcoinData.TEST01_VALID_AMOUNT);
			service.pay(r, ap);
			List<Registration> allRs = service.getAllRegistrations();
			assertEquals(allRs.size(), 1);
			bitcoinAsserts(ap, allRs.get(0).getBitcoin());

		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	@Test
	public void test_04_testMultiplePaysBreakNegative() {
		int breakIndex = TestPaymentWithBitcoinData.TEST04_BREAK_INDEX;
		String[] ids = TestPaymentWithBitcoinData.TEST04_VALID_IDS;
		int[] amounts = TestPaymentWithBitcoinData.TEST04_PARTIAL_BREAK_AMOUNTS;
		String[] names = TestPaymentWithBitcoinData.TEST04_PERSON_NAMES;
		String[] events = TestPaymentWithBitcoinData.TEST04_EVENT_NAMES;
		int length = ids.length;

		Bitcoin[] pays = new Bitcoin[length];

		try {
			for (int i = 0; i < length; i++) {
				Person person = TestUtils.setupPerson(service, names[i]);
				Event event = TestUtils.setupEvent(service, events[i]);
				Registration r = TestUtils.register(service, person, event);
				pays[i] = service.createBitcoinPay(ids[i], amounts[i]);
				service.pay(r, pays[i]);
			}
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithBitcoinData.AMOUNT_NEGATIVE_ERROR, e.getMessage());
			List<Registration> allRs = service.getAllRegistrations();
			assertEquals(allRs.size(), breakIndex + 1);
			for (int i = 0; i < breakIndex; i++) {
				if (!contains(allRs, pays[i])) {
					fail();
				}
			}
		}
	}

	@Test
	public void test_05_testUpdatePay() {
		try {
			Person person = TestUtils.setupPerson(service, TestPaymentWithBitcoinData.TEST05_PERSON_NAME);
			Event event = TestUtils.setupEvent(service, TestPaymentWithBitcoinData.TEST05_EVENT_NAME);
			Registration r = TestUtils.register(service, person, event);
			Bitcoin ap1 = service.createBitcoinPay(TestPaymentWithBitcoinData.TEST05_INITIAL_ID,
					TestPaymentWithBitcoinData.TEST05_INITIAL_AMOUNT);
			Bitcoin ap2 = service.createBitcoinPay(TestPaymentWithBitcoinData.TEST05_AFTER_ID,
					TestPaymentWithBitcoinData.TEST05_AFTER_AMOUNT);
			service.pay(r, ap1);
			List<Registration> allRs1 = service.getAllRegistrations();
			assertEquals(allRs1.size(), 1);
			bitcoinAsserts(ap1, allRs1.get(0).getBitcoin());
			service.pay(r, ap2);
			List<Registration> allRs2 = service.getAllRegistrations();
			assertEquals(allRs2.size(), 1);
			bitcoinAsserts(ap2, allRs2.get(0).getBitcoin());
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	@Test
	public void test_06_testCreateBitcoin() {
		try {
			Bitcoin ap = service.createBitcoinPay(TestPaymentWithBitcoinData.TEST06_VALID_ID,
					TestPaymentWithBitcoinData.TEST06_VALID_AMOUNT);
			assertEquals(1, bitcoinRepository.count());
			for (Bitcoin pay : bitcoinRepository.findAll()) {
				bitcoinAsserts(ap, pay);
			}
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	@Test
	public void test_08_testPayWithRegistrationNull() {
		try {
			Person person = TestUtils.setupPerson(service, TestPaymentWithBitcoinData.TEST08_PERSON_NAME);
			Event event = TestUtils.setupEvent(service, TestPaymentWithBitcoinData.TEST08_EVENT_NAME);
			Registration r = TestUtils.register(service, person, event);
			Bitcoin ap = null;
			service.pay(r, ap);
			fail();

		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithBitcoinData.PAY_WITH_NULL_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_09_testCreateBitcoinWrongFormat() {
		try {
			service.createBitcoinPay(TestPaymentWithBitcoinData.TEST09_WRONG_ID,
					TestPaymentWithBitcoinData.TEST09_VALID_AMOUNT);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithBitcoinData.ID_FORMAT_ERROR, e.getMessage());
		}
	}

	@Test
	public void tst_10_testCreateBitcoinLongFormat() {
		try {
			service.createBitcoinPay(TestPaymentWithBitcoinData.TEST10_WRONG_ID,
					TestPaymentWithBitcoinData.TEST10_VALID_AMOUNT);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithBitcoinData.ID_FORMAT_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_11_testCreateBitcoinNull() {
		try {
			service.createBitcoinPay(TestPaymentWithBitcoinData.TEST11_WRONG_ID,
					TestPaymentWithBitcoinData.TEST11_VALID_AMOUNT);
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithBitcoinData.ID_FORMAT_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_12_testCreateBitcoinEmpty() {
		try {
			service.createBitcoinPay(TestPaymentWithBitcoinData.TEST12_WRONG_ID,
					TestPaymentWithBitcoinData.TEST12_VALID_AMOUNT);
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithBitcoinData.ID_FORMAT_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_13_testCreateBitcoinSpace() {
		try {
			service.createBitcoinPay(TestPaymentWithBitcoinData.TEST13_WRONG_ID,
					TestPaymentWithBitcoinData.TEST13_VALID_AMOUNT);
		} catch (IllegalArgumentException e) {
			assertEquals(TestPaymentWithBitcoinData.ID_FORMAT_ERROR, e.getMessage());
		}
	}

	@Test
	public void test_14_testCreateBitcoinZero() {
		try {
			Bitcoin ap = service.createBitcoinPay(TestPaymentWithBitcoinData.TEST14_VALID_ID,
					TestPaymentWithBitcoinData.TEST14_VALID_AMOUNT);
			assertEquals(1, bitcoinRepository.count());
			for (Bitcoin pay : bitcoinRepository.findAll()) {
				bitcoinAsserts(ap, pay);
			}
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	// Util Methods, no test
	public void bitcoinAsserts(Bitcoin expected, Bitcoin actual) {
		assertNotEquals(null, actual);
		assertEquals(expected.getAmount(), actual.getAmount());
		assertEquals(expected.getUserID(), actual.getUserID());
	}

	public boolean bitcoinEquals(Bitcoin pay1, Bitcoin pay2) {
		return pay2 != null && pay2.getAmount() == pay1.getAmount() && pay2.getUserID().equals(pay1.getUserID());
	}

	public boolean contains(List<Registration> rs, Bitcoin pay) {
		for (Registration r : rs) {
			if (bitcoinEquals(pay, r.getBitcoin())) {
				return true;
			}
		}
		return false;
	}
}
