package com.example.sudokusolver;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button lastClickedButton = null;
    private int[][] grid = new int[9][9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Declaring Variables
        Button btsub;
        Button btr;

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btsub = findViewById(R.id.buttons);
        btr = findViewById(R.id.buttonr);

        // Main Logic for Button 1 to 89
        for (int i = 1; i <= 89; i++) {
            // Generate the button ID dynamically
            int resID = getResources().getIdentifier("button" + i, "id", getPackageName());
            if (resID != 0) {
                Button button = findViewById(resID);
                if (button != null) {
                    button.setOnClickListener(v -> {
                        handleSudokuDigits(button);
                    });
                }
            }
        }

        // Main Logic for Button a to j
        for (char c = 'a'; c <= 'j'; c++) {
            String buttonId = "button" + c;
            int resID = getResources().getIdentifier(buttonId, "id", getPackageName());
            if (resID != 0) {
                Button button = findViewById(resID);
                if (button != null) {
                    button.setOnClickListener(v -> {
                        if(lastClickedButton==null){
                            Toast.makeText(this, "Please select a digit", Toast.LENGTH_SHORT).show();
                        }else{
                            handleInputsButton(button);
                        }
                    });
                }
            }
        }

        // Reset
        btr.setOnClickListener(v -> {
            resetGrid();
        });

        //Main Logic for submit button
        btsub.setOnClickListener(v -> {
            getGridInput();
//            for(int row=0;row<9;row++){
//                for(int col=0;col<9;col++){
//                    System.out.println(row+" "+col+" "+grid[row][col]);
//                }
//            }
            if(validateInput(grid)){
                if (solveSudoku(grid, 0, 0)) {
                    setGridOutput();
                }
                else {
//                    System.out.println("No Solution exists");
                    Toast.makeText(this, "No Solution exists", Toast.LENGTH_SHORT).show();
                }
            }
//            System.out.println("Output");
//            for(int row=0;row<9;row++){
//                for(int col=0;col<9;col++){
//                    System.out.println(row+" "+col+" "+grid[row][col]);
//                }
//            }
        });
    }

    private void handleSudokuDigits(Button clickedButton) {
        // Reset the last clicked button if it's not null and is not the current button
        if (lastClickedButton != null && lastClickedButton != clickedButton) {
            lastClickedButton.setTextSize(20);

            // Resolve the color foreground from the current theme
            TypedValue typedValue = new TypedValue();
            TypedValue typedValue1 = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.colorForeground, typedValue, true);
            getTheme().resolveAttribute(android.R.attr.colorBackground, typedValue1, true);
            int colorForeground = typedValue.data;
            int colorBackground = typedValue1.data;

            lastClickedButton.setTextColor(colorForeground);
            lastClickedButton.setBackgroundColor(colorBackground);
        }

        // Change the color and size of the clicked button
        clickedButton.setTextSize(24);
        clickedButton.setTextColor(getResources().getColor(R.color.header));
        clickedButton.setBackgroundColor(getResources().getColor(R.color.bg_clicked));

        // Update the lastClickedButton reference
        lastClickedButton = clickedButton;
    }

    private void handleInputsButton(Button clickedButton) {
        if (clickedButton.getId() == R.id.buttona) {
            lastClickedButton.setText("1");
        } else if (clickedButton.getId() == R.id.buttonb) {
            lastClickedButton.setText("2");
        } else if (clickedButton.getId() == R.id.buttonc) {
            lastClickedButton.setText("3");
        } else if (clickedButton.getId() == R.id.buttond) {
            lastClickedButton.setText("4");
        } else if (clickedButton.getId() == R.id.buttone) {
            lastClickedButton.setText("5");
        } else if (clickedButton.getId() == R.id.buttonf) {
            lastClickedButton.setText("6");
        } else if (clickedButton.getId() == R.id.buttong) {
            lastClickedButton.setText("7");
        } else if (clickedButton.getId() == R.id.buttonh) {
            lastClickedButton.setText("8");
        } else if (clickedButton.getId() == R.id.buttoni) {
            lastClickedButton.setText("9");
        } else if (clickedButton.getId() == R.id.buttonj) {
            lastClickedButton.setText("0");
        }
    }

    private void resetGrid(){
        for (int i = 1; i <= 89; i++) {
            // Generate the button ID dynamically
            int resID = getResources().getIdentifier("button" + i, "id", getPackageName());
            if (resID != 0) {
                Button button = findViewById(resID);
                button.setText("0");
            }
        }
    }

    private void getGridInput() {
        int buttonIndex = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                buttonIndex++;
                if (buttonIndex % 10 == 0) {
                    buttonIndex++;
                }
//                System.out.println("Bi Input"+buttonIndex);

                int resID = getResources().getIdentifier("button" + buttonIndex, "id", getPackageName());
                if (resID != 0) {
                    Button button = findViewById(resID);
                    if (button != null) {
                        String btText = button.getText().toString();
                        try {
                            grid[row][col] = Integer.parseInt(btText);
                        }
                        catch (NumberFormatException e) {
                                grid[row][col] = 0;
                        }
                    }
                }
            }
        }
    }

    private void setGridOutput(){
        int buttonIndex = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                buttonIndex++;

                if (buttonIndex % 10 == 0) {
                    buttonIndex++;
                }

                // Check if button exists and update its text
                int resID = getResources().getIdentifier("button" + buttonIndex, "id", getPackageName());
                if (resID != 0) {
                    Button button = findViewById(resID);
                    if (button != null) {
                        int gridValue = grid[row][col];
                        button.setText(String.valueOf(gridValue));
                    }
                }
            }
        }
    }





    /* Takes a partially filled-in grid and attempts to assign values to all unassigned locations in
    such a way to meet the requirements for Sudoku solution (non-duplication across rows,columns, and boxes) */
    private boolean solveSudoku(int grid[][], int row, int col)
    {
        //if we have reached the 8th row and 9th column (0 indexed matrix) ,we are returning true to avoid further backtracking
        if (row == 9 - 1 && col == 9)
            return true;
        // Check if column value  becomes 9 ,we move to next row and column start from 0
        if (col == 9) {
            row++;
            col = 0;
        }
        // Check if the current position of the grid already contains value >0, we iterate for next column
        if (grid[row][col] != 0)
            return solveSudoku(grid, row, col + 1);

        for (int num = 1; num < 10; num++) {

            // Check if it is safe to place the num (1-9)  in the given row ,col ->we move to next column
            if (isSafe(grid, row, col, num)) {

                //  assigning the num in the current (row,col)  position of the grid and assuming our assigned num in the position is correct
                grid[row][col] = num;

                // Checking for next possibility with next column
                if (solveSudoku(grid, row, col + 1))
                    return true;
            }
            // removing the assigned num , since our assumption was wrong , and we go for next assumption with diff num value
            grid[row][col] = 0;
        }
        return false;
    }

    //    Check whether it will be legal to assign num to the given row, col
    private boolean isSafe(int[][] grid, int row, int col, int num) {
        // Check if we find the same num in the same row, excluding the current cell
        for (int x = 0; x <= 8; x++) {
            if (x != col && grid[row][x] == num) {
                return false;
            }
        }
        // Check if we find the same num in the same column, excluding the current cell
        for (int x = 0; x <= 8; x++) {
            if (x != row && grid[x][col] == num) {
                return false;
            }
        }
        // Check the 3x3 subgrid, excluding the current cell
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if ((i + startRow != row || j + startCol != col) && grid[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        return true;
    }


    // Validate the grid for initial input errors
    private boolean validateInput(int[][] grid) {
        // STEP 1: Check grid size (Not Necessary in App)
//        if (grid.length != 9 || grid[0].length != 9) {
////            System.out.println("Error: The grid size must be 9x9.");
//            Toast.makeText(this, "Invalid Grid Size", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        // STEP 2: Check for valid numbers and existing conflicts
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int num = grid[i][j];
//                (Not Necessary in App)
//                if (num < 0 || num > 9) {
//                    System.out.println("Error: Numbers must be between 0 and 9.");
//                    return false;
//                }

                // STEP 3: Check if the initial number causes a conflict (ignore 0 as it's unassigned)
                if (num != 0 && !isSafe(grid, i, j, num)) {
//                    System.out.println("NUMM: "+num);
//                    System.out.println("Error: Invalid Sudoku grid. Conflict at row " + (i + 1) + ", column " + (j + 1));
                    Toast.makeText(this, "Conflicting Row", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }
}





