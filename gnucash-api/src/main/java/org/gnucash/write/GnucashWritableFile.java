/**
 * GnucashWritableFile.java
 * Created on 16.05.2005
 * (c) 2005 by "Wolschon Softwaredesign und Beratung".
 * <p>
 * Permission is granted to use, modify, publish and sub-license this code
 * as specified in the contract. If nothing else is specified these rights
 * are given non-exclusively with no restrictions solely to the contractor(s).
 * If no specified otherwise I reserve the right to use, modify, publish and
 * sub-license this code to other parties myself.
 * <p>
 * Otherwise, this code is made available under GPLv3 or later.
 * <p>
 * -----------------------------------------------------------
 * major Changes:
 * 16.05.2005 - initial version
 * ...
 */
package org.gnucash.write;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.gnucash.generated.GncV2;
import org.gnucash.numbers.FixedPointNumber;
import org.gnucash.read.GnucashAccount;
import org.gnucash.read.GnucashCustomer;
import org.gnucash.read.GnucashFile;
import org.gnucash.read.GnucashJob;

/**
 * created: 16.05.2005 <br/>
 *
 * Extension of GnucashFile that allows writing. <br/>
 * All the instances for accounts,... it returns can be assumed
 * to implement the respetive *Writable-interfaces.
 *
 * @see GnucashFile
 * @see org.gnucash.write.impl.GnucashFileWritingImpl
 * @author <a href="mailto:Marcus@Wolschon.biz">Marcus Wolschon</a>
 *
 */
public interface GnucashWritableFile extends GnucashFile, GnucashWritableObject {

	/**
	 * @return true if this file has been modified.
	 */
	boolean isModified();

	/**
	 * The value is guaranteed not to be bigger then the maximum of
	 * the current system-time and the modification-time in the file
	 * at the time of the last (full) read or sucessfull write.<br/
	 * It is thus suitable to detect if the file has been modified outside of
	 * this library
	 * @return the time in ms (compatible with File.lastModified) of the last write-operation
	 */
	long getLastWriteTime();

	/**
	 * @param pB true if this file has been modified.
	 * @see {@link #isModified()}
	 */
	void setModified(boolean pB);


	/**
	 * Write the data to the given file.
	 * That file becomes the new file returned by
	 * {@link GnucashFile#getGnucashFile()}
	 * @param file the file to write to
	 * @throws IOException kn io-poblems
	 */
	void writeFile(File file) throws IOException;

	/**
	 * @return the underlying JAXB-element
	 */
	GncV2 getRootElement();


	/**
	 * @param id the unique id of the customer to look for
	 * @return the customer or null if it's not found
	 */
	GnucashWritableCustomer getCustomerByID(String id);


	/**
	 *
	 * @return a read-only collection of all accounts that have no parent
	 */
	Collection<? extends GnucashWritableAccount> getWritableRootAccounts();

	/**
	 *
	 * @return a read-only collection of all accounts
	 */
	Collection<? extends GnucashWritableAccount> getWritableAccounts();


	/**
	 * @see GnucashFile#getTransactionByID(String)
	 * @return A changable version of the transaction.
	 */
	GnucashWritableTransaction getTransactionByID(String id);

	/**
	 * @see GnucashFile#getInvoiceByID(String)
	 * @param id the id to look for
	 * @return A changable version of the invoice.
	 */
	GnucashWritableInvoice getInvoiceByID(String id);

	/**
	 * @see GnucashFile#getAccountByName(String)
	 * @param name the name to look for
	 * @return A changable version of the account.
	 */
	GnucashWritableAccount getAccountByName(String name);

	/**
	 * @param type the type to look for
	 * @return A changable version of all accounts of that type.
	 */
	Collection<GnucashWritableAccount> getAccountsByType(String type);

	/**
	 * @see GnucashFile#getAccountByID(String)
	 * @param id the id of the account to fetch
	 * @return A changable version of the account or null of not found.
	 */
	GnucashWritableAccount getAccountByID(String id);

	/**
	 * @see GnucashFile#getJobByID(String)
	 * @param jobID the id of the job to fetch
	 * @return A changable version of the job or null of not found.
	 */
	GnucashWritableJob getJobByID(String jobID);

	/**
	 * @param jnr the job-number to look for.
	 * @return the (first) jobs that have this number or null if not found
	 */
	GnucashWritableJob getJobByNumber(final String jnr);

	/**
	 * @return all jobs as writable versions.
	 */
	Collection<GnucashWritableJob> getWritableJobs();

	/**
	 * Add a new currency.<br/>
	 * If the currency already exists, add a new price-quote for it.
	 * @param pCmdtySpace the namespace (e.g. "GOODS" or "ISO4217")
	 * @param pCmdtyId the currency-name
	 * @param conversionFactor the conversion-factor from the base-currency (EUR).
	 * @param pCmdtyNameFraction number of decimal-places after the comma
	 * @param pCmdtyName common name of the new currency
	 */
	public void addCurrency(final String pCmdtySpace, final String pCmdtyId, final FixedPointNumber conversionFactor, final int pCmdtyNameFraction, final String pCmdtyName);

	/**
	 * @see GnucashFile#getTransactions()
	 * @return writable versions of all transactions in the book.
	 */
	Collection getWritableTransactions();

	/**
	 * @return a new transaction with no splits that is already added to this file
	 */
	GnucashWritableTransaction createWritableTransaction();

	/**
	 * @return a new transaction with no splits that is already added to this file
	 */
	GnucashWritableTransaction createWritableTransaction(final String id);


	/**
	 *
	 * @param impl the transaction to remove.
	 */
	void removeTransaction(GnucashWritableTransaction impl);


	/**
	 * @return a new customer with no values that is already added to this file
	 */
	GnucashWritableCustomer createWritableCustomer();

	/**
	 * @return a new job with no values that is already added to this file
	 */
	GnucashWritableJob createWritableJob(final GnucashCustomer customer);

	/**
	 * @return a new job with no values that is already added to this file
	 */
	GnucashWritableJob createWritableJob(final String id, final GnucashCustomer customer);


	/**
	 * @return a new account that is already added to this file as a top-level account
	 */
	GnucashWritableAccount createWritableAccount();

	/**
	 * @return a new account that is already added to this file as a top-level account
	 */
	GnucashWritableAccount createWritableAccount(final String id);

	/**
	 * @return a new invoice with no entries that is already added to this file
	 */
	GnucashWritableInvoice createWritableInvoice(final String invoiceNumber,
												 final GnucashJob job,
												 final GnucashAccount accountToTransferMoneyTo,
												 final java.util.Date dueDate);

	/**
	 * FOR USE BY EXTENSIONS ONLY
	 * @return a new invoice with no entries that is already added to this file
	 */
	GnucashWritableInvoice createWritableInvoice(final String internalID,
												 final String invoiceNumber,
												 final GnucashJob job,
												 final GnucashAccount accountToTransferMoneyTo,
												 final java.util.Date dueDate);

	/**
	 * @param impl the account to remove
	 */
	void removeAccount(GnucashWritableAccount impl);

	/**
	 * THIS METHOD IS ONLY TO BE USED BY EXTENSIONS TO THIS LIBRARY!<br/>
	 * @return a new customer
	 * @param id the id the customer shall have
	 */
	GnucashWritableCustomer createWritableCustomer(String id);


}
