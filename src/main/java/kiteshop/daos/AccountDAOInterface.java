package kiteshop.daos;

import java.util.List;

import kiteshop.pojos.Account;

//test test

public interface AccountDAOInterface {

	void createAccount(Account account);

	String givePassword(String gebruiker);
	
	
	Account readAccountByGebruikersnaam(String gebruikersnaam);
	
	List<Account> readAllAccounts();
	
	void updateAccount(Account account);
	
	void deleteAccount(Account account);

}