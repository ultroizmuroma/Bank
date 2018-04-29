package ru.barinov.bank;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.barinov.bank.general.ServiceAnswer;
import ru.barinov.bank.model.Credit;
import ru.barinov.bank.repository.CountryRestrictionRepository;
import ru.barinov.bank.repository.CreditRepository;
import ru.barinov.bank.repository.CustomerBlackListRepository;
import ru.barinov.bank.repository.PropertiesRepository;
import ru.barinov.bank.credit.CreditApprover;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class BankApplicationTests {
	@Autowired
	private EntityManager entityManager;
	@Autowired private CreditRepository creditRepository;
	@Autowired private CustomerBlackListRepository customerBlackListRepository;
	@Autowired private CountryRestrictionRepository countryRestrictionRepository;
	@Autowired private PropertiesRepository propertiesRepository;

	@Test
	public void addCredit() {
		CreditApprover creditApprover = new CreditApprover(creditRepository, customerBlackListRepository, countryRestrictionRepository, propertiesRepository);
		Credit credit = new Credit();
		credit.setCustomerId(3L);
		credit.setCustomerFirstName("Иван");
		credit.setCustomerSurname("Иванов");
		credit.setRepaymentDate(new Date());
		credit.setCountry("Украина");
		credit.setAmount(new BigDecimal(100.33).setScale(2,  BigDecimal.ROUND_CEILING));
		Assert.assertEquals(ServiceAnswer.APPROVE.toString(), creditApprover.processCredit(credit));
	}

	@Test
	public void addCreditWithEmptyFields() {
		CreditApprover creditApprover = new CreditApprover(creditRepository, customerBlackListRepository, countryRestrictionRepository, propertiesRepository);
		Credit credit = new Credit();
		Assert.assertEquals(ServiceAnswer.APPROVE.toString(), creditApprover.processCredit(credit));
	}

	@Test
	public void checkCreditLimit() {
		propertiesRepository.setRequestLimit(3);
		propertiesRepository.setTrackingInterval(3);
		CreditApprover creditApprover = new CreditApprover(creditRepository, customerBlackListRepository, countryRestrictionRepository, propertiesRepository);
		int limit = propertiesRepository.getRequestLimit();
		Credit credit = new Credit();
		credit.setCustomerId(3L);
		credit.setCustomerFirstName("Иван");
		credit.setCustomerSurname("Иванов");
		credit.setRepaymentDate(new Date());
		credit.setCountry("Украина");
		credit.setAmount(new BigDecimal(100.33).setScale(2,  BigDecimal.ROUND_CEILING));
		for (int i = 0; i < limit; i++) {
			Assert.assertEquals(ServiceAnswer.APPROVE.toString(), creditApprover.processCredit(credit));
		}
		Assert.assertEquals(ServiceAnswer.REJECT_COUNTRY_BANNED.toString(), creditApprover.processCredit(credit));
		try {
			Thread.sleep(propertiesRepository.getTrackingInterval() * 1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(ServiceAnswer.APPROVE.toString(), creditApprover.processCredit(credit));
	}

	@Test
	public void getCreditList() {
		List<Credit> list = (ArrayList<Credit>) creditRepository.findAll();
		Assert.assertEquals(2, list.size());
	}

	@Test
	public void getCreditListForCustomer() {
		List<Credit> list = (ArrayList<Credit>) creditRepository.findAllByCustomerId(1L);
		Assert.assertEquals(1, list.size());
		for (Credit credit : list) {
			Assert.assertEquals(1L, credit.getId() != null ? credit.getId() : 0L);
			Assert.assertEquals(1L, credit.getCustomerId() != null ? credit.getCustomerId() : 0L);
			Assert.assertEquals("Алексей", credit.getCustomerFirstName());
			Assert.assertEquals("Баринов", credit.getCustomerSurname());
			Assert.assertEquals(1527714000000L, credit.getRepaymentDate().getTime());
			Assert.assertEquals("Россия", credit.getCountry());
			Assert.assertEquals(new BigDecimal(10000.55).setScale(2, BigDecimal.ROUND_CEILING), credit.getAmount());
		}
	}

	@Test
	public void checkCustomerInBlackList() {
		Boolean normal = customerBlackListRepository.isCustomerBanned(1L);
		Boolean blocked = customerBlackListRepository.isCustomerBanned(2L);
		Assert.assertEquals(false, normal);
		Assert.assertEquals(true, blocked);
	}

	@Test
	public void addAndDeleteCustomerToBlackList() {
		Long customerId = 1L;
		customerBlackListRepository.addCustomer(customerId);
		Assert.assertEquals(true, customerBlackListRepository.isCustomerBanned(customerId));
		customerBlackListRepository.deleteCustomer(customerId);
		Assert.assertEquals(false, customerBlackListRepository.isCustomerBanned(customerId));
	}
}
