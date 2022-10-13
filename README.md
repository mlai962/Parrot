# Parrot
How to run:
1. Clone the repo, open a terminal, and cd to the directory containing the Parrot.jar which is the git repo root.
2. Execute the jar using the command "java -jar Parrot.jar"
3. The program takes quite a while to run (2 mins) because I had to make webClient.waitForBackgroundJavaScript(4000) calls which waits for the page to reload whenever we click the load more shows button in order to get rid of errors.
4. The program will print to terminal the shows and urls as well as write the required csv file which is called shows.csv.

Testing:
I did manual testing by checking random shows on the website with rows of the output csv. For example, I made sure the first show on the website matched with the first show in the csv, I did this for multiple shows, including shows that require clicking the load more shows button. This testing is clearly non-comprehensive since it doesn't test if we have actually read in every show on the site but I couldn't come up with a way to test this. 