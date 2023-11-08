# hw1- Manual Review

The homework will be based on this project named "Expense Tracker",where users will be able to add/remove daily transaction. 

## Compile

To compile the code from terminal, use the following command:
```
cd src
javac ExpenseTrackerApp.java
java ExpenseTracker
```

You should be able to view the GUI of the project upon successful compilation. 

## Java Version
This code is compiled with ```openjdk 17.0.7 2023-04-18```. Please update your JDK accordingly if you face any incompatibility issue. 

## New Features in [Version/Release]

### Undo Functionality

- **Undo Last Transaction**: Users can now undo the last added transaction. This feature allows for the immediate retraction of a mistakenly entered transaction without having to manually delete it.
  
  **Usage**:
  - To undo the last transaction, simply click the 'Undo' button on the main interface. If no transaction has been added since the application was started or since the last undo, the button will be disabled to prevent errors.

### Filtering Enhancements

- **Filter by Amount**: Transactions can now be filtered by entering a specific amount. This helps in quickly finding transactions of a particular value.
  
  **Usage**:
  - Enter the amount in the 'Filter by Amount' field and click the 'Filter' button to apply the filter. To clear the filter, remove the amount and click the 'Filter' button again.

- **Filter by Category**: This feature allows users to filter the transaction list by categories such as 'Food', 'Utilities', 'Entertainment', etc.

  **Usage**:
  - Select the desired category from the drop-down menu and click the 'Filter' button to apply the filter. To see all transactions again, select 'All' from the drop-down menu and click the 'Filter' button.
  
 ## Testing

To ensure the reliability and stability of the Expense Tracker application, a comprehensive suite of automated test cases is included. Here is an overview of the types of tests performed:

### Test Cases Included

- **Add Transaction Test**: Verifies that a user can add a valid transaction and that the total cost is updated accordingly.

- **Invalid Input Handling Test**: Ensures that the application handles invalid inputs gracefully without affecting the transaction list or total cost.

- **Filter by Amount Test**: Confirms that transactions can be filtered by amount, and only transactions matching the filter criteria are shown.

- **Filter by Category Test**: Tests the functionality to filter transactions by categories, ensuring that only the transactions of a specific category are displayed.

- **Undo Disallowed Test**: Checks the application's behavior when an undo operation is attempted without any transactions to undo, expecting no changes to occur.

- **Undo Allowed Test**: Ensures that the application can undo the most recently added transaction and update the total cost accordingly.

These test cases are implemented using JUnit and form part of the continuous integration pipeline to catch any regressions or issues early in the development process.
