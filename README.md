# java-GUI-interface
A java GUI interface, designed to add and manipulate searching query. Return and show the result of matching wine samples.

## Functionality
There is the initial interface, it has only one editable button and some guide word.
##### Step1: 
Click the “Choose csv file” button, and select the file that you what to choose together in once time at the popup window( Sorry about Chinese in picture, it should be English in your PC), and then click “open”.
##### Step2: 
Then choose conditions in the drop down box and input number in text field; Click the “main information” button can display some information of samples.
##### Step3: 
Click the “initial” button to input the conditions and Searching query will display on the show area.
##### Step4: 
If you want to add another condition, choose in drop down boxes and then click button ”Add Condition”, new searching query will display on show area; If you want to end inputing conditions, click button “Add Finish”.
##### Step5: 
Click button “Get Result” to get result and display in the show area; If you want to start another searching, clici button “Reset” and don’t need to choose file again.


## Aspect of design
	1.CentreFrame Class: Used to make a new frame displayed in the center, it extends the  JFrame.
	2.void initial(): Used to initialize the JPanel, JButton, JCombobox and JTextarea.
	3.void actionPerformed(): Used to write the action listener function to deal the different event in button and drop down box.
	4.void actionPerformed() — choose csv button: Use chooser to popup a new window to choose the files which need read in an array, then get their path and use BufferedReader to input the files.
	5.void actionPerformed() — main information button: Display the outcome like highest quality and the number of the samples.
	6.void actionPerformed() — initial button: Input a searching query with select, kind, where and condition in STR.
	7.void actionPerformed() — Add continue button: Add some conditions after the searching query.
	8.void actionPerformed() — Add finish button: Finish add condition and display the final searching query. Unlock the "Get Result” button.
	9.void actionPerformed() — Get Result button: Get the final result.
	10.void actionPerformed() — Reset button: Clear the STR and show area, make the button’s enable attribute is the original status before manipulate.
