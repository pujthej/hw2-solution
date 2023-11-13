// package test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import controller.ExpenseTrackerController;
import controller.InputValidation;
import model.ExpenseTrackerModel;
import model.Transaction;
import model.Filter.AmountFilter;
import view.ExpenseTrackerView;
import model.Filter.CategoryFilter;



public class TestExample {
  
  private ExpenseTrackerModel model;
  private ExpenseTrackerView view;
  private ExpenseTrackerController controller;

  @Before
  public void setup() {
    model = new ExpenseTrackerModel();
    view = new ExpenseTrackerView();
    controller = new ExpenseTrackerController(model, view);
  }

    public double getTotalCost() {
        double totalCost = 0.0;
        List<Transaction> allTransactions = model.getTransactions(); // Using the model's getTransactions method
        for (Transaction transaction : allTransactions) {
            totalCost += transaction.getAmount();
        }
        return totalCost;
    }


    public void checkTransaction(double amount, String category, Transaction transaction) {
	assertEquals(amount, transaction.getAmount(), 0.01);
        assertEquals(category, transaction.getCategory());
        String transactionDateString = transaction.getTimestamp();
        Date transactionDate = null;
        try {
            transactionDate = Transaction.dateFormatter.parse(transactionDateString);
        }
        catch (ParseException pe) {
            pe.printStackTrace();
            transactionDate = null;
        }
        Date nowDate = new Date();
        assertNotNull(transactionDate);
        assertNotNull(nowDate);
        // They may differ by 60 ms
        assertTrue(nowDate.getTime() - transactionDate.getTime() < 60000);
    }


    @Test
    public void testAddTransaction() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());
    
        // Perform the action: Add a transaction
	    double amount = 50.0;
	    String category = "food";
        assertTrue(controller.addTransaction(amount, category));
    
        // Post-condition: List of transactions contains only
	//                 the added transaction	
        assertEquals(1, model.getTransactions().size());
    
        // Check the contents of the list
	Transaction firstTransaction = model.getTransactions().get(0);
	checkTransaction(amount, category, firstTransaction);
	
	// Check the total amount
        assertEquals(amount, getTotalCost(), 0.01);
    }


    @Test
    public void testRemoveTransaction() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());
    
        // Perform the action: Add and remove a transaction
	double amount = 50.0;
	String category = "food";
        Transaction addedTransaction = new Transaction(amount, category);
        model.addTransaction(addedTransaction);
    
        // Pre-condition: List of transactions contains only
	//                the added transaction
        assertEquals(1, model.getTransactions().size());
	Transaction firstTransaction = model.getTransactions().get(0);
	checkTransaction(amount, category, firstTransaction);

	assertEquals(amount, getTotalCost(), 0.01);
	
	// Perform the action: Remove the transaction
        model.removeTransaction(addedTransaction);
    
        // Post-condition: List of transactions is empty
        List<Transaction> transactions = model.getTransactions();
        assertEquals(0, transactions.size());
    
        // Check the total cost after removing the transaction
        double totalCost = getTotalCost();
        assertEquals(0.00, totalCost, 0.01);
    }

    @Test
    public void testAddFoodTransaction() {
    // Pre-condition: Ensure the transaction list is initially empty
        assertEquals("The transaction list should be empty before adding a new transaction.", 0, model.getTransactions().size());

    // Action: Add a transaction with amount 50.00 and category "food"
        double amount = 50.0;
        String category = "food";
        boolean addTransactionResult = controller.addTransaction(amount, category);

    // Verify: Check if the transaction was added successfully
        assertTrue("The transaction should be added successfully.", addTransactionResult);

    // Post-condition: The transaction list should contain the new transaction
        List<Transaction> transactions = model.getTransactions();
        assertEquals("The transaction list should contain one transaction after adding.", 1, transactions.size());

    // Verify: The transaction details are correct
        Transaction transaction = transactions.get(0);
        checkTransaction(amount, category, transaction);

    // Verify: The total cost is updated correctly
        double totalCost = getTotalCost();
        assertEquals("The total cost should be updated to reflect the added transaction amount.", amount, totalCost, 0.01);
    }

    @Test
    public void testInvalidInputHandling() {
    // Pre-condition: Ensure the transaction list is initially empty
        assertEquals("The transaction list should be empty before attempting to add an invalid transaction.", 0, model.getTransactions().size());

    // Save the initial total cost
        double initialTotalCost = getTotalCost();

    // Action: Attempt to add a transaction with an invalid amount (e.g., -50.0) and invalid category (e.g., "")
        double invalidAmount = -50.0; // Negative amount to represent an invalid case
        String invalidCategory = ""; // Empty string to represent an invalid case
        boolean addTransactionResult = controller.addTransaction(invalidAmount, invalidCategory);

    // Verify: Adding transaction should fail
        assertFalse("Adding an invalid transaction should fail.", addTransactionResult);

    // Post-condition: Verify the transaction list is still empty
        List<Transaction> transactions = model.getTransactions();
        assertEquals("The transaction list should remain empty after attempting to add an invalid transaction.", 0, transactions.size());

    // Verify: Total cost should remain unchanged
        double totalCostAfterAttempt = getTotalCost();
        assertEquals("The total cost should remain unchanged after attempting to add an invalid transaction.", initialTotalCost, totalCostAfterAttempt, 0.01);
    }
    @Test
    public void testFilterByAmount() {
    // Pre-condition: Ensure the transaction list is initially empty
        assertEquals("The transaction list should be empty before adding transactions.", 0, model.getTransactions().size());
        double filterAmount = 50.0;
        // Add Transactions
        model.addTransaction(new Transaction(100.0, "food"));
        model.addTransaction(new Transaction(filterAmount,"food"));
        model.addTransaction(new Transaction(200.0, "entertainment"));
        model.addTransaction(new Transaction(filterAmount,"entertainment"));
        model.addTransaction(new Transaction(50.0,"bills"));

        // Create an AmountFilter
        AmountFilter amtFilter = new AmountFilter(50.0);

        // Apply the filter to list of transactions
        List<Transaction> filteredTransactions = amtFilter.filter(model.getTransactions());

        // Post conditions: Assert that the filtered list only contains transactions with the amount = amountToFilter
        assertNotNull("Filtered transactions list should not be null", filteredTransactions);
        assertEquals("There should be three transactions with the amount equal to 50.0", 3, filteredTransactions.size());

        // Verify that all transactions in the filtered list have the amount equal to 50.0
        for (Transaction transaction : filteredTransactions) {
            assertEquals("The filtered transaction should have amount equal to 50.0", filterAmount, transaction.getAmount(), 0.01);
        }
    }

    @Test
    public void testFilterByCategory() {
        // Pre-condition: Ensure the transaction list is initially empty
        assertEquals("The transaction list should be empty before adding transactions.", 0, model.getTransactions().size());
        String filterCategory = "food";
        // Add Transactions
        model.addTransaction(new Transaction(100.0, "food"));
        model.addTransaction(new Transaction(100.0,filterCategory));
        model.addTransaction(new Transaction(200.0, "entertainment"));

        // Create a categoryFilter
        CategoryFilter catFilter = new CategoryFilter("food");

        // Print debug information
        System.out.println("All Transactions: " + model.getTransactions());

        // Apply the filter to list of transactions
        List<Transaction> filteredTransactions = catFilter.filter(model.getTransactions());

        // Post conditions: Assert that the filtered list only contains transactions with the category = food
        assertNotNull("Filtered transactions list should not be null", filteredTransactions);
        // Print debug information
        System.out.println("Number of filtered transactions: " + filteredTransactions.size());
        assertEquals("There should be two transactions with the category equal to food", 2, filteredTransactions.size());

        // Verify that all transactions in the filtered list have food as category
        for (Transaction transaction : filteredTransactions) {
            assertEquals("The filtered transaction should have category equal to food", filterCategory, transaction.getCategory());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUndoDisallowedOnEmptyList() {
        // Pre-condition: Ensure the transaction list is initially empty
        assertEquals("The transaction list should be empty before attempting to undo.", 0, model.getTransactions().size());

    
        // Action: Attempt to undo when the transaction list is empty
        // We'll simulate this by calling removeTransaction with an empty index array
        controller.removeTransaction(new int[] {});
    
        // Expected Output: An IllegalArgumentException should be thrown
        // The expected exception is specified in the @Test annotation
    }
    @Test
    public void testUndoTransaction() {
        // Setup: Add a transaction
        double amount = 25.0;
        String category = "entertainment";
        boolean transactionAdded = controller.addTransaction(amount, category);
        assertTrue("Transaction should be added before it can be undone", transactionAdded);
    
        // Ensure there is a transaction to remove
        assertEquals(1, model.getTransactions().size());
    
        // Execution Steps: Get the index of the last transaction added and Undo the transaction addition
        int lastIndex = model.getTransactions().size() - 1;
        controller.removeTransaction(new int[] {lastIndex});
        
        // Assertions: Check that the transaction is removed and total cost is updated
        assertEquals(0, model.getTransactions().size());
        assertEquals(0.00, getTotalCost(), 0.01);
    }
    

    
    
    
    
    
    
    
    
    
    
    




    


    
}
