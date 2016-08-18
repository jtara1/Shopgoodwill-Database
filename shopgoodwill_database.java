import java.util.*;
import java.util.List;
import java.io.*;
/*package need to import URL class*/
import java.net.*;
/* import Jsoup for parsing html */
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/* packages that are need for GUI interface */
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.EventQueue;
/* packages needed to download images */
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
/* packages needed to get current date and time */
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*public class for shopgoodwill database */
@SuppressWarnings("serial")
public class Phase3 extends JFrame {
 /*global String List that will hold all the category names for the database */
 /*the category names are the keys for the category ID numbers in categoryHashtable */
 public static java.util.List<String>            globalCategoryKeys = new ArrayList<String>();
 /*global String List that will hold all the location names for the database */
 /*the location names are the keys for the location ID numbers in locationHashtable */
 public static java.util.List<String>            globalLocationKeys = new ArrayList<String>();
 /* Hashtbale were each pair will have a category name as its key and the ID number as its value */
 public static Hashtable<String, String>         categoryHashtable;
 /* Hashtbale were each pair will have a location  name as its key and the ID number as its value */
 public static Hashtable<String, String>         locationHashtable;
 /* hashtable that will hold the auction number as the key and information about its retrieval as the value */
 public static Hashtable<String, String>         searchesHashtable = new Hashtable<String, String>();
 /* searchText will hold the current text in the search text field  */
 public static JTextField                        searchText;
 /* ComboBox(drop down menu) that will list all the possible locations to search in*/
 public static JComboBox<String>                 locationCombo; 
 /* ComboBox(drop down menu) that will list all the possible categories to search in*/
 public static JComboBox<String>                 categoryCombo;
 /* StringBuffer that will hold message to print at the end of search */
 /* message will return the auctions that fit search query  and the location of the processed auctions*/
 public static StringBuffer                      searchMessage = new StringBuffer("");
 /* String that will hold the previously used search query*/
 public static String                            searchTerms;
 /* int that will hold the number of search results from the previous search */
 public static int                               searchResults = 0;
 /* String that will hold the the numbers of miles entered*/
 public static JTextField                        milesText;
 /* String that will hold the zipcde entered*/
 public static JTextField                        zipcodeText;
 /* 2D String array that will hold the all the data values in the zipcode.csv file  */
 public static String[][]                        zipcodeCSV = null;
 /* Hashtable that holds all the locations names as the keys*/
 /* The values are the zipcodes that each key is assigned according to the zipcode.csv*/
 public static Hashtable<String, String>         locationZipcode = new Hashtable<String, String>();
 /* double needed to convert result to kilometers in haversine formula method*/
 public static final double                      R = 6372.8; 
 /* String to hold the current location of the auction being saved */
 public static String                            currentLocation = new String();
 /* seriailized hashtable for data storage of all past searches */
 public static File                              serializedSearches = new File("searches.ser");

 /*call function to initialize shopgoodwill database GUI */
 public Phase3() {
  initUI();      
 }

 /*initialize values for shopgoodwill database GUI*/
 private void initUI() {
  /*create frame and set its name to Shopgoodwill Database*/
  JFrame frame = new JFrame("Shopgoodwill Database");
  /*set the size for the Shoppgoodwill Database*/
  frame.setSize(950, 225);
  /*set the defualt location to be null (middle of the screen)*/
  frame.setLocationRelativeTo(null);
  /*exit program when user presses close button*/
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  /*make the GUI interface visible*/
  frame.setVisible(true);

  /*create new panel*/
  JPanel panel = new JPanel();
  /*add panel to frame */
  frame.add(panel);

  /*set default layout (nothing on frame)*/
  panel.setLayout(null);

  /*create a label (text that will appear next to search field) and set it to "Search For" */
  JLabel searchLabel = new JLabel("Search for");
  /*set the x, y coordinates, width and height for label*/
  searchLabel.setBounds(10, 10, 80, 25);
  /*add label to panel */
  panel.add(searchLabel);

  /*create a search field and set the default size(20) */
  searchText = new JTextField(20);
  /*set the x,y coordinates, width and height for the search field*/
  searchText.setBounds(100, 10, 800, 25);
  /*add the search field to the panel */
  panel.add(searchText);

  /*create a label (text that will appear next to drop down menu) and set it to "Category" */
  JLabel categoryLabel = new JLabel("Category");
   /*set the x,y coordinates, width and height for the label*/
  categoryLabel.setBounds(10, 50, 80, 25);
  /* add label to panel */
  panel.add(categoryLabel);

  /*create a ComboBox (drop down menu)*/
  DefaultComboBoxModel<String> categoryModel = new DefaultComboBoxModel<String>();
  categoryCombo = new JComboBox<String>(categoryModel);
  /*set the x,y coordinates, width and height for the comboBox*/
  categoryCombo.setBounds(100, 50, 800, 25);
  /* go through all the category keys (category names) */
  /* add category as item ComboBox (drop down menu) */
  for(String c: globalCategoryKeys) {
   categoryCombo.addItem(c);
  }
  /* add ComboBox to Panel */
  panel.add(categoryCombo);

   /*create a label (text that will appear next to drop down menu) and set it to "Location" */
  JLabel locationLabel = new JLabel("Location");
   /*set the x,y coordinates, width and height for the label*/
  locationLabel.setBounds(10, 90, 80, 25);
  /* add label to panel */
  panel.add(locationLabel);

  /*create a ComboBox (drop down menu)*/
  DefaultComboBoxModel<String> locationModel = new DefaultComboBoxModel<String>();
  locationCombo = new JComboBox<String>(locationModel);
  /*set the x,y coordinates, width and height for the comboBox*/
  locationCombo.setBounds(100, 90, 800, 25);
  /* go through all the location keys (category names) */
  /* add category as item ComboBox (drop down menu) */
  for(String l: globalLocationKeys) {
   locationCombo.addItem(l);
  }
  /* add label to panel */
  panel.add(locationCombo);

  /*create a label (text that will appear next to drop down menu) and set it to "Location" */
  JLabel milesLabel = new JLabel("Search for sellers within");
   /*set the x,y coordinates, width and height for the label*/
  milesLabel.setBounds(10, 130, 200, 25);
  /* add label to panel */
  panel.add(milesLabel);

  /*create a search field and set the default size(20) */
  milesText = new JTextField(20);
  /*set the x,y coordinates, width and height for the search field*/
  milesText.setBounds(200, 130, 70, 25);
  /*add the search field to the panel */
  panel.add(milesText);

  /*create a label (text that will appear next to drop down menu) and set it to "Location" */
  JLabel zipcodeLabel = new JLabel("miles of zipcode:");
   /*set the x,y coordinates, width and height for the label*/
  zipcodeLabel.setBounds(290, 130, 150, 25);
  /* add label to panel */
  panel.add(zipcodeLabel);

  /*create a search field and set the default size(20) */
  zipcodeText = new JTextField(20);
  /*set the x,y coordinates, width and height for the search field*/
  zipcodeText.setBounds(430, 130, 70, 25);
  /*add the search field to the panel */
  panel.add(zipcodeText);

  /*create button that user will use to activate search with current query and selected category, location values*/
  JButton searchButton = new JButton("Search");
  /*set the x,y coordinates, width and height for the button*/
  searchButton.setBounds(100, 160, 150, 25);
  /* add button to panel */
  panel.add(searchButton);

  /* create button that user will use to bring up table with category names and their ID numbers*/
  JButton categoryButton = new JButton("Category Table");
  /*set the x,y coordinates, width and height for the button*/
  categoryButton.setBounds(250, 160, 150, 25);
  /* add button to panel */
  panel.add(categoryButton);

  /* create button that user will use to bring up table with location/seller names and their ID numbers*/
  JButton locationButton = new JButton("Location Table");
  /*set the x,y coordinates, width and height for the button*/
  locationButton.setBounds(400, 160, 150, 25);
  /* add button to panel */
  panel.add(locationButton);

  /* create that a button that will be used to find and process all auctions stored in data storage */
  JButton rebuildButton = new JButton("Rebuild Database");
  /*set the x,y coordinates, width and height for the button*/
  rebuildButton.setBounds(550, 160, 180, 25);
  /* add button to panel */
  panel.add(rebuildButton);

  /* perorm an action when user exits gui window */
  frame.addWindowListener(new WindowAdapter() { 
   @Override
   public void windowClosing(WindowEvent e) {
    try {
     /*if searchesHashtable has new data, save it to searchlog.txt */
     if(!searchesHashtable.isEmpty())       
      outputSearchLog(new File("searchlog.txt"), searchesHashtable);
    } catch(IOException f) {
    }
   }});          

  /*wait and perform an action when search button is pressed */
  searchButton.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    try {  
     /* if user does not want to find a location within a certain amount of miles of a specific zipcode */
     if(milesText.getText().isEmpty() && zipcodeText.getText().isEmpty() ) {
      /*process the search results*/ 
      saveSearchResults();
      /*show  a message about the results have been processed*/
      showSearchMessage(searchMessage, 1);
      /*reset the number of search results for next search*/
      searchResults = 0;
      /*erase the message for the next search*/
      searchMessage.setLength(0);
      /* save search data to searchesHashtable */
      serializeHashtable(searchesHashtable, serializedSearches);
     } 
    /*if user decided to use the miles within zipcode search engine */
    else { 
     /* if zipcode.csv is not in current working directory inform the user */
     if (zipcodeCSV == null) 
      JOptionPane.showMessageDialog(null, "Cannot use zipcode locator because zipcode.csv is not in current working directory");
    /*check if miles field is empty  */ 
     else if(milesText.getText().isEmpty()) 
      JOptionPane.showMessageDialog(null, "Enter a value for the number of miles within the zipcode");
     /* check if zipcode field is empty */
     else if(zipcodeText.getText().isEmpty()) 
      JOptionPane.showMessageDialog(null, "Enter a value for the zipcode");
    /* check if miles field has a valid number */
     else if(!(milesText.getText().matches("[1-9][0-9]{0,8}")))   
      JOptionPane.showMessageDialog(null, "Enter a valid number of miles");
    /* check if zipcode field has a zipcode that exists */
     else if(isValidZipcode() == false) 
      JOptionPane.showMessageDialog(null, "Zipcode is not valid");
     /* return search results acorrding to miles and zipcode entered along with search query and category */
     else {
      /* get search results based on zipcode */
      saveZipcodeSearchResults();
      /* show message of saved files */
      showSearchMessage(searchMessage, 2);
      searchResults = 0;
      searchMessage.setLength(0);
      /*store information about saved files in data storage */
      serializeHashtable(searchesHashtable, serializedSearches);
     }
    }
   } catch(IOException f) {
    }
   }
  }); 

  /*wait and perform an action when the category button is pressed */
  categoryButton.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
  /*display a table with category names and their ID numbers */
    createCategoryTable();
   }
  });
  
  /*wait and perform an action when the location button is pressed */
  locationButton.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
  /*display a table with location names and their ID numbers */ 
    createLocationTable();
   }
  });
  
  /*wait and perform an action when the rebuild button is pressed */
  rebuildButton.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    /* if there is no data storage file in current working directory show inform user */
    if(searchesHashtable.isEmpty()) 
     JOptionPane.showMessageDialog(null, "No data storage(searches.ser) in current working directory");
   /* process and save all auctions saved in the data storage */
    else
     try {
      int option; 
      /* ask user if they are sure about rebuilding database*/
      option = JOptionPane.showConfirmDialog(null, "Are you sure you want to rebuild the database?", "Message",  JOptionPane.YES_NO_OPTION);
      /* rebuild database if user presses yes button, otherwise do nothing*/
      if(option == JOptionPane.YES_OPTION) {
       rebuildDatabase(); 
       showSearchMessage(searchMessage, 3);
       searchMessage.setLength(0);
      } 
      else
        ;
     //serializeHashtable(searchesHashtable, serializedSearches);
     } catch(IOException f) {
     }
   }
  });
 }

 /* java implementation of haversine formula as seen on http://rosettacode.org/wiki/Haversine_formula */
 /* return distance between to locations in km, which will have to be converted to miles */
 public static double haversine(double lat1, double lon1, double lat2, double lon2) {
  double dLat = Math.toRadians(lat2 - lat1);
  double dLon = Math.toRadians(lon2 - lon1);
  lat1 = Math.toRadians(lat1);
  lat2 = Math.toRadians(lat2);

  double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) *
   Math.cos(lat2);
  double c = 2 * Math.asin(Math.sqrt(a));
  return R * c;
 }

 /* return the latitude of a zipcode */
 public static String getZipcodeLatitude(String zipcode) {
  String latitude = new String();

  /* flag to check whether latitude was found or not */
  boolean notFound = true;
  
  /* iterate through zipcodCSV array*/    
  for(int i = 0; i < zipcodeCSV.length; i++) {
   /* if row is not empty */
   if(zipcodeCSV.length != 1) {
    /* if zipcode matches and latitude was not found*/
    if(notFound && ((zipcodeCSV[i][0]).replaceAll("\"", "")).equals(zipcode)) { 
     /* get latitude from zipcodeCSV array */
     latitude = (zipcodeCSV[i][3]).replaceAll("\"", "");
     /* latitude was found */
     notFound = false;
   //  System.out.println((zipcodeCSV[i][3]).replaceAll("\"", "") + zipcode);
   }
   }
  }
  
  /* return latitude */
  return latitude;
 }

 /* return the longitude of a zipcode */
 public static String getZipcodeLongitude(String zipcode) {
  String longitude = new String();

  /* flag to check whether longitude was found or not */
  boolean notFound = true;

  /* iterate through zipcodCSV array*/
  for(int i = 0; i < zipcodeCSV.length; i++) {
   /* if row is not empty */
   if(zipcodeCSV.length != 1) {
    /* if zipcode matches and longitude was not found*/
    if(notFound && ((zipcodeCSV[i][0]).replaceAll("\"", "")).equals(zipcode)) {
     /* get longitude from zipcodeCSV array */
     longitude = (zipcodeCSV[i][4]).replaceAll("\"", "");
     /* longitude was found */
     notFound = false;
    }
   }
  }
  /* return longitude */
  return longitude;
 }

 /* checks to see if zipcode entered into the zipcode is an existing zipcode in the zipcode.csv file */
 public static boolean isValidZipcode() {
  /* iterate through zipcodeCSV*/
  for(int j = 0; j < zipcodeCSV.length; j++) { 
  /* return true if there is a match */
  if( ( (zipcodeCSV[j][0]).replaceAll("\"", "")     ).equals(zipcodeText.getText()) )
    return true;
  }
 
  return false; 
 }

 /* use auction ID numbers stored in data storage to retrieve and process all past search results */
 public static void rebuildDatabase() throws MalformedURLException, IOException{
  try {
   /* get keys(auction ID numbers) of searchesHashtable and iterate through them */
   Set<String> keys = searchesHashtable.keySet();
   for(String key: keys) {
    /* create URL for searching by auction ID number */
    String URL = new String("http://www.shopgoodwill.com/viewItem.asp?itemID=" + key);

    /* connect to the auction URL*/
    Document doc = Jsoup.connect(URL).get();
   
    /* get the entire html page in one element*/
    Element body = doc.getElementsByTag("html").first();
   
    /*get the user's current working directory */
    String workingDirectory = System.getProperty("user.dir");
   
    /*create a directory to for all auctions if it doesn't exist */
    File auctionDirectory = new File(workingDirectory + "/Auctions");

    /* if directory for all auctions does not exist yet, create it*/
    if(!auctionDirectory.exists())
     auctionDirectory.mkdir();

    /* create a new directory with the name of the auction ID number*/
    File auctionDir = new File(auctionDirectory.getName() + '/' + key);
    auctionDir.mkdir();

    /*get the name of the HTML file for the auction*/
    Element canonical = body.getElementsByTag("link").first();
    String canonicalHref = canonical.attr("href");
    int index = canonicalHref.lastIndexOf('/');
    int end   = canonicalHref.lastIndexOf(".html");
    String filename = canonicalHref.substring(index + 1, end) + ".txt";

    /* create a BufferedWriter to write to the file */
    String filenm = new String(workingDirectory + '/' + auctionDirectory.getName() + '/' + auctionDir.getName() + '/' +
    filename);
    File output = new File(filenm);
    FileWriter fw = new FileWriter(output, false);
    BufferedWriter bw = new BufferedWriter(fw);
    /* write all the text from the auction page to the file */
    bw.write(body.text() + '\n');

   /*display to the prompt that the file was saved and its location*/
   System.out.println(filename + " was saved to " + auctionDir.getAbsolutePath());

   /* close BufferedWriter */
   bw.close();
   /* get 'itemdetaildesc2' becuase it is the location of the auction images */
   /* get all the of its 'a' tags (where the images are kept) */
   Element imageClass  = body.getElementsByClass("itemdetaildesc2").first();
   Element imageBody   = imageClass.children().first();
   Elements imagesURL  = imageBody.getElementsByTag("a");

   int numberOfImages = 0;
   /* iterate through the images */
   for(Element image: imagesURL) {
    /* create a new instance of Buffered Image to hold the auction image*/
    BufferedImage imageFile = null;

    /* get the URL of the image from the href attriubute of the 'a' tag*/
    URL imageSource = new URL(image.attr("href"));

    /* read the URL to the imagefile*/
    imageFile = ImageIO.read(imageSource);

    /* get the first index of the image's filename in the URL*/
    int imageURLIndex = (imageSource.toString()).lastIndexOf('/');

  /*get the filename of the URL */
    String imageFilename = (imageSource.toString()).substring(imageURLIndex + 1);

     /*print that the file was saved and its location to the prompt */
    System.out.println(imageFilename + " was saved to " +  auctionDir.getAbsolutePath());

    /* write the image to the filename in its directory */
    ImageIO.write(imageFile, "JPG",new File(workingDirectory + '/' + auctionDirectory.getName() + '/' + auctionDir.getName()    + '/'  + imageFilename));

    numberOfImages = numberOfImages + 1;
   }
   /* a append a string to the searchMessage that says location of the auction*/
   searchMessage.append("Auction #" + auctionDir.getName() + " was saved to " + auctionDir.getAbsolutePath() + " with " + numberOfImages + " images" + '\n');
  }
  } 
  catch(IOException e) {
  }
 }

 /*display message in a GUI window after search has been processed*/
 /*inform the user of how many auctions fit the criteria and of the location of the processed auctions */
 public static void showSearchMessage(StringBuffer searchMessage, int typeOfMessage) {
  /* create a text area and set the defualt size*/
  JTextArea textArea = new JTextArea(20, 40);
  /*enter message into text area*/
  /* different message if user used miles within zipcode search engine*/
  /* */
  if(typeOfMessage == 1)
  textArea.setText("Your search for " + searchTerms + " in category:" + categoryCombo.getSelectedItem() + " at location:" +
   locationCombo.getSelectedItem() + " returned " + searchResults + " results" + '\n' + searchMessage.toString());
  /* */
  else if(typeOfMessage == 2)
   textArea.setText("Your search for " + searchTerms + " in category:" + categoryCombo.getSelectedItem() + " within " +
   milesText.getText() + " miles of zipcode:" + zipcodeText.getText() +  " returned " + searchResults + " results" + '\n' + searchMessage.toString());
  /* */
  else if(typeOfMessage == 3) 
    textArea.setText(searchMessage.toString());
  /*message is not editiable by user */
  textArea.setEditable(false);
  /* make the window scrollable */
  JScrollPane scrollPane = new JScrollPane(textArea);
  /* display the message */
  JOptionPane.showMessageDialog(null, scrollPane);
 }

  /*create a JTable with the names of all the categories and their ID numbers */
 public static void createCategoryTable() {
  /* create an instance of a frame and name it category Table */
  JFrame frame = new JFrame("Category Table");
  /* set the width, height */
  frame.setSize(900, 300);
  /* set the background color */
  frame.setBackground(Color.gray);
  /* set the location (middle of the screen) */
  frame.setLocationRelativeTo(null);
  /* make window visible*/
  frame.setVisible(true);

  /* create a JPanel and add it to the frame*/
  JPanel panel = new JPanel();
  frame.add(panel);

  /* set a container for the panel*/
  panel.setLayout(new BorderLayout());
  
  /* add panel to the container */
  frame.getContentPane().add(panel);

  /*create an array with the necessary names for the columns needed for the table constructor */
  String columnNames[] = {"Category", "ID Number"};

  /* create a 2D array with the number of rows equal to the size of the category hashtable */ 
  String dataPair[][] = new String[categoryHashtable.size()][];
  
  /* current index for the dataPair array that will increase every iteration */
  int index = 0;

  /* iterate through every key(category name) in globalCategoryKeys*/
  for(String key :globalCategoryKeys) {
   /* use key to get value in category Hashtable */
   String value = categoryHashtable.get(key);
  
   /* create an array using the key and value and enter that array into the 2D dataPair array */
   String pair[]  = {key, value};
   dataPair[index] = pair;
   
   /*increase index by one */
   index++;
  }
   
  /* create a new table using the above choosen column names and filled with the dataPair values */
  JTable table = new JTable(dataPair, columnNames);
  
  /* create a  scrollable panel for the table */
  JScrollPane scrollPane = new JScrollPane(table);
  /* add a scrollable panel to the already existing panel */
  panel.add(scrollPane, BorderLayout.CENTER);
  
  /*set the width for the columns, the category name column should be wider than the category ID number column*/
  table.getColumnModel().getColumn(0).setPreferredWidth(800);
  table.getColumnModel().getColumn(1).setPreferredWidth(120);
 }
 
 /*create a JTable with the names of all the locations/sellers and their ID numbers */
 public static void createLocationTable() {
  /* create an instance of a frame and name it "Location Table" */
  JFrame frame = new JFrame("Location Table");
  /* set the width, height */
  frame.setSize(900, 300);
  /* set the background color */
  frame.setBackground(Color.gray);
  /* set the location (middle of the screen) */
  frame.setLocationRelativeTo(null);
  /* make window visible*/
  frame.setVisible(true);
 
  /* create a JPanel and add it to the frame*/
  JPanel panel = new JPanel();
  frame.add(panel);

  /* set a container for the panel*/
  panel.setLayout(new BorderLayout());
  frame.getContentPane().add(panel);
    
  /*create an array with the necessary names for the columns needed for the table constructor */
  String columnNames[] = {"Location", "ID Number"};

  /* create a 2D array with the number of rows equal to the size of the category hashtable */
  String dataPair[][] = new String[locationHashtable.size()][];
  
  /* current index for the dataPair array that will increase every iteration */
  int index = 0;

  /* iterate through every key(location name) in globalLocationKeys*/
  for(String key :globalLocationKeys) {
   /* use key to get value in cat */
   String value = locationHashtable.get(key);

   /* create array using the  key and value and enter that array into 2D dataPair array */
   String pair[]  = {key, value};
   dataPair[index] = pair;
   
   /* increase index by one */
   index++;
  }

  /* create a new table using the above choosen column names and filled with dataPair values */
  JTable table = new JTable(dataPair, columnNames);
  
  /* create scrollable panel for table */
  JScrollPane scrollPane = new JScrollPane(table);
  /* add scrollable panel to the  already existing panel */
  panel.add(scrollPane, BorderLayout.CENTER);

  /*set  the width for the columns, the category name column should be wider than the category ID number column*/
  table.getColumnModel().getColumn(0).setPreferredWidth(800);
  table.getColumnModel().getColumn(1).setPreferredWidth(120);
 }

 /* method that will process the search results */
 /* method will go through the search results HTML page and extract the links to the auctions*/
 /* extracted auction links will then be passed to saveAuctionInformation method*/
 public static void saveSearchResults() throws MalformedURLException, IOException {
  try {
 /* get the current selected item in the category Combobox and use it to get the category ID number from categoryHashtbale*/
   String catKey    = categoryCombo.getSelectedItem().toString();
   String catID     =  categoryHashtable.get(catKey); 
 /* get the curren selected item in the location Combobox and use it to get the location ID number from locationHashtbale*/
   String sellerKey =  locationCombo.getSelectedItem().toString();
   String sellerID  = locationHashtable.get(sellerKey);
 
   /* get current location to save to searchesHashtable */
   currentLocation = sellerKey;

   /*get the current text in the search field so it can be used in the URL */
   String itemTitle = searchText.getText(); 

   /* save the current text in the search field so it can be used in the message */
   searchTerms      = searchText.getText();

   /* replace the spaces in the the search query with '+', as needed for the URL */
   String itemTitle2 = itemTitle.replace(" ", "+");

   /* used to go the first page of the search results */ 
   int auctionPage = 1;

  /* do not anything if all search query is empty and categeory = "All Categories" and location = "All Sellers" */
  /* this returns an error on the shopgoodwill website*/
  if(searchText.getText().isEmpty() && categoryCombo.getSelectedItem().equals("All Categories") && 
   locationCombo.getSelectedItem().equals("All Sellers"))
   ;
   
   else 
    while(true) { 
    /*connect to shopgoodwill using current search query, location, categories, and page number */
     Document doc = Jsoup.connect("http://www.shopgoodwill.com/search/SearchKey.asp?itemtitle=" + 
      itemTitle2  + "&catID=" + catID + "&sellerid=" + sellerID + "&page=" + auctionPage).get();
  
     /*get everything in first div tag of class "mainbluebox" */
     Element mainbluebox = doc.select("div.mainbluebox").first();
     /*get elements in tag tbody (this is where auction links are kept */
     Elements tbody = mainbluebox.getElementsByTag("tbody");
     /*get 'a' tags with href attributes (auction links) */
     Elements auctionLinks = tbody.select("a[href]");

     /* if there are are no auction links, exit method */
     if(auctionLinks.isEmpty()) 
      break;
    
     /* iterate through each 'a' tag */
     for(Element link: auctionLinks) {
      /* pass a href attributes (auction URLs) in 'a' tags to the method */
      saveAuctionInformation(link.attr("href"));
      /* increase the number of search results by 1 (this is needed for the end of search message) */
      searchResults = searchResults + 1;
     }   
    
     /* move on to the next page of search results*/
     auctionPage = auctionPage + 1;
    }
  } catch(IOException e) {
  }
 }
  
 /* auction directory will be created in user's current working directory and store all auction that were searched for*/
 /* create a directory for each auction, the directory name will be the auction ID number */
 /* each auction directory will have a file with all text from auction page and all the auction item images*/ 
 public static void saveAuctionInformation(String URL) throws MalformedURLException, IOException {
  try {
   /* connect to the auction URL that was passed from the previous method*/
   Document doc = Jsoup.connect(URL).get();
   /* get the entire html page in one element*/
   Element body = doc.getElementsByTag("html").first();
   /*get the user's current working directory */
   String workingDirectory = System.getProperty("user.dir");
   /*create a directory to for all auctions if it doesn't exist */
   File auctionDirectory = new File(workingDirectory + "/Auctions");
   
   /* create auction directory if doesn't exist */
   if(!auctionDirectory.exists()) 
    auctionDirectory.mkdir();

   /*get starting index for the name of the auction HTML file in the URL */
   int index = URL.lastIndexOf('/');
 
   /* create a new directory with the name of the auction ID number*/
   File auctionDir = new File(auctionDirectory.getName() + '/' + URL.substring(URL.length() - 13, URL.length() - 5));
   auctionDir.mkdir();

   /* a append a string to the searchMessage that says location of the auction*/
   searchMessage.append("Auction #" + auctionDir.getName() + " was saved to " + auctionDir.getAbsolutePath() + '\n');
   
    /*get the name of the HTML file for the auction*/
   String filename = URL.substring(index + 1, URL.length() - 5) + ".txt";
 
    /* create a BufferedWriter to write to the file */
   String filenm = new String(workingDirectory + '/' + auctionDirectory.getName() + '/' + auctionDir.getName() + '/' +  
   filename);   
   File output = new File(filenm);
   FileWriter fw = new FileWriter(output, false);
   BufferedWriter bw = new BufferedWriter(fw);
   /* write all the text from the auction page to the file */
   bw.write(body.text() + '\n'); 
  
   /*get current date and time to save to searches data storage */
   DateFormat Dformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
   Calendar calendar = Calendar.getInstance();
  
   /*display to the prompt that the file was saved and its location*/
   System.out.println(filename + " was saved to " + auctionDir.getAbsolutePath());

   /* close BufferedWriter */
   bw.close();
   /* get 'itemdetaildesc2' becuase it is the location of the auction images */
   /* get all the of its 'a' tags (where the images are kept) */
   Element imageClass  = body.getElementsByClass("itemdetaildesc2").first();
   Element imageBody   = imageClass.children().first();
   Elements imagesURL  = imageBody.getElementsByTag("a");

   int numberOfImages = 0;
   /* iterate through the images */
   for(Element image: imagesURL) {
    /* create a new instance of Buffered Image to hold the auction image*/
    BufferedImage imageFile = null;

    /* get the URL of the image from the href attriubute of the 'a' tag*/
    URL imageSource = new URL(image.attr("href"));

    /* read the URL to the imagefile*/
    imageFile = ImageIO.read(imageSource);

    /* get the first index of the image's filename in the URL*/
    int imageURLIndex = (imageSource.toString()).lastIndexOf('/');

    /*get the filename of the URL */
    String imageFilename = (imageSource.toString()).substring(imageURLIndex + 1);
    
     /*print that the file was saved and its location to the prompt */
    System.out.println(imageFilename + " was saved to " +  auctionDir.getAbsolutePath());
    
    /* write the image to the filename in its directory */
    ImageIO.write(imageFile, "JPG",new File(workingDirectory + '/' + auctionDirectory.getName() + '/' + auctionDir.getName()    + '/'  + imageFilename));   
   numberOfImages++;
  }
 
/* save auction number and information about auction to searchesHashtable*/
searchesHashtable.put(URL.substring(URL.length() - 13, URL.length() - 5), "Auction #" + auctionDir.getName() + " was found using search terms:" +  searchText.getText() + " in category:" + categoryCombo.getSelectedItem().toString() + " at location:" +  currentLocation + " and was saved to " + auctionDir.getAbsolutePath() + " with " +  numberOfImages + " images on " + Dformat.format(calendar.getTime()) + '\n');
  
  } catch(IOException e) {
  }
 }

 /* returns search results that were within a user chosen number of miles of a user chosen zipcode */
 public static void saveZipcodeSearchResults() throws NumberFormatException, MalformedURLException , IOException {
  try {
  /* get the current chosen category and use it as key to return its ID number */
  String catKey    = categoryCombo.getSelectedItem().toString();
  String catID     =  categoryHashtable.get(catKey);

  /*get the current text in the search field so it can be used in the URL */
  String itemTitle = searchText.getText();

  /* save the current text in the search field so it can be used in the message */
  searchTerms  = searchText.getText();

  /* replace the spaces in the the search query with '+', as needed for the URL */
  String itemTitle2 = itemTitle.replace(" ", "+");
 // System.out.println(itemTitle2);

  /* get the latitude and longitude of the zipcode the user entered */
  String latitude1 = new String();
  String longitude1 = new String();  
  latitude1 = getZipcodeLatitude(zipcodeText.getText()); 
  longitude1 = getZipcodeLongitude(zipcodeText.getText());

  /* convert the latitude and longitude of the zipcode to doubles */
  double lat1 = Double.parseDouble(latitude1);
  double lon1 = Double.parseDouble(longitude1);

  /* iterate through all possible locations */
  /* location will be compared to the user chosen zipcode*/
  for(String location: globalLocationKeys) {
  /* if location is not equal to "All Sellers" check if location fits criteria */
  if(!location.equals("All Sellers")) {
  currentLocation = location;
  /* get the sellerID to be used in the url */
  String sellerID = locationHashtable.get(location);

  /* get the first page of auctions */
   int auctionPage = 1;
     
   /* get the zipcode of the location */
   String locationString =  location;
   String zipcode =  locationZipcode.get(locationString);    
   
   /* get the latitude and longitude of the zipcode */
   String latitude2 = getZipcodeLatitude(zipcode);
   String longitude2 = getZipcodeLongitude(zipcode);
  // System.out.println(latitude2 + "  " +  longitude2);  

   /* convert the longitude and the latitude to doubles */
   double lat2 = Double.parseDouble(latitude2);
   double lon2 = Double.parseDouble(longitude2);
 
  /* convert the number of miles within the zipcode to an integer */
   int milesWithin = Integer.parseInt(milesText.getText());

   /* if the location fits the search criteria download auctions with that location and  fit search query and categories */
   if(haversine(lat1, lon1, lat2, lon2) * 0.62137 <= milesWithin) {
    // System.out.println(haversine(lat1, lon1, lat2, lon2));
    boolean auctionLinkAvailable = true;
    while(auctionLinkAvailable) {
    /*connect to shopgoodwill using current search query, location, categories, and page number */
     Document doc = Jsoup.connect("http://www.shopgoodwill.com/search/SearchKey.asp?itemtitle=" +
      itemTitle2  + "&catID=" + catID + "&sellerid=" + sellerID + "&page=" + auctionPage).get();
 
    /*get everything in first div tag of class "mainbluebox" */
     Element mainbluebox = doc.select("div.mainbluebox").first();
    /*get elements in tag tbody (this is where auction links are kept */
     Elements tbody = mainbluebox.getElementsByTag("tbody");
    /*get 'a' tags with href attributes (auction links) */
     Elements auctionLinks = tbody.select("a[href]");

     /* if there are are no auction links, exit method */
     if(auctionLinks.isEmpty())
      auctionLinkAvailable = false;
     
      else {
     /* iterate through each 'a' tag */
     for(Element link: auctionLinks) {
     /* pass a href attributes (auction URLs) in 'a' tags to the method */
      saveAuctionInformation(link.attr("href"));
     /* increase the number of search results by 1 (this is needed for the end of search message) */
      searchResults = searchResults + 1; 
      }
     }

     /* move on to the next page of search results*/
     auctionPage = auctionPage + 1;
    } 
   }
  }
 } 
 } catch(IOException e) { 
  }
   catch(NumberFormatException n) {
  }  
 } 

 /*connect to shopgoodwill and load information to category and location hashtabels */
 public static void GetInformation(Hashtable<String, String> category, Hashtable<String, String> location) throws MalformedURLException, IOException {
  try {
   /* connect to shopgoodwill search and get webpage as document */
   Document doc = Jsoup.connect("http://www.shopgoodwill.com/search/").get(); 

   /* get parent elements by id that indicates if they are a category or seller/location */
   Element parentTagOfCategories = doc.getElementById("catid");
   Element parentTagOfLocations = doc.getElementById("SellerID");
  
   /* get all the individual elements in the */
   Elements categories = parentTagOfCategories.children();
   Elements locations = parentTagOfLocations.children();
 
   /* iterate through tags for each category and get their name and value for each attribute (key and value for Hashtbale)*/
   for(Element c: categories) 
    category.put(c.text(), c.attr("value"));
  
   /* iterate through tags for each location/seller and get their name and value attribute (key and value for Hashtbale*/
   for(Element l: locations)
    location.put(l.text(), l.attr("value"));

  } catch(IOException e) {
  }
 } 

 /* return a list of the keys in the hashtable in alphabetical order */
 public static List<String> sortKeys(Hashtable<String, String> htable) { 
  /*get the keys in a hashtable */
  Enumeration<String> keys = htable.keys();
  /* put the hashtable keys in a list*/
  java.util.List<String> keyList = Collections.list(keys);
  /*sort them alphabetically and return */
  Collections.sort(keyList);
  return keyList;
 }

 /* output the keys and values of a hashtable to a text file */
 public static void outputKeysValues(Hashtable<String, String> htable, List<String> keys, File output) throws IOException { 
  try {
   /* create a BufferedWriter to filename that was passed to method*/
   FileWriter fw = new FileWriter(output, false);
   BufferedWriter bw = new BufferedWriter(fw); 
   /* use list of keys to iterate through hashtable and get their values*/
   /* write keys and values to the BufferedWriter */
   for(String k: keys) 
    bw.write(k + " value:" + htable.get(k)  + '\n');
   /* close BufferedWriter */
   bw.close();
  } catch(IOException e) {
  }
 }

 /* save HTML file of http://www.shopgoodwill.com/search/ to current working directory */
 public static void GetSearchHTML() throws MalformedURLException, IOException {
  try {
   /* create new file with the name of the search html file*/
   File file = new File("search.html");

   /* create a BufferedWriter for the created file*/
   FileWriter fw = new FileWriter(file.getName(), false);
   BufferedWriter bw = new BufferedWriter(fw);
 
   /* connect to http://www.shopgoodwill.com/search/ URL and create a BufferedReader for Webpage */
   BufferedReader br =  new BufferedReader(new InputStreamReader(new URL("http://www.shopgoodwill.com/search/").openStream())   );  
   
   /* read Webpage line by line*/
   String line = br.readLine(); 
  
   /* While there is still a line left to read, write the line to BufferedWriter and read nextline */
   while(line != null) {
    bw.write(line + '\n');
    line = br.readLine();
   }
  
   /* close BufferedReader and BufferedWriter */
   br.close();
   bw.close();
  } catch(IOException e) {
  }
 }
  
 /* serialize Hashtable  */
 /* serialized Hashtables are used for the purpose of persistent data storage */
 public static void serializeHashtable(Hashtable<String, String> htable, File file) {
  try {
   /* create a stream to chosen file*/
   FileOutputStream fileOut = new FileOutputStream(file);
   ObjectOutputStream out = new ObjectOutputStream(fileOut);
   /* write the hashtable to the file */
   out.writeObject(htable);
   out.close();
   /* close the stream to the file*/
   fileOut.close();
  } catch(IOException i) {
   i.printStackTrace();
  }
 }

 /* deserialize the seriazlied Hashtable passed to the method and return it*/
 @SuppressWarnings("unchecked")
 public static Hashtable<String, String> deserializeHashtable(File serializedFile) {
  /* create hashtbale*/
  Hashtable<String, String> htable = new Hashtable<String, String>();
  try {
   /* create stream to serilized hashtable file  */
   FileInputStream fileIn = new FileInputStream(serializedFile);
   ObjectInputStream in = new ObjectInputStream(fileIn);
   /* write serialized hashtable to hashtable*/
   htable  = (Hashtable<String, String>) in.readObject();
   /* close stream */
   in.close();
   fileIn.close();
  } catch(IOException i) {
   i.printStackTrace();
  } catch(ClassNotFoundException c) {
   System.out.println("ERROR: cannot find Hashtable class");
   c.printStackTrace();
  }
  /* return deserialized hashtable*/
  return htable;
 }

 /* get the data in zipcode.csv file and put in a 2D array */
 public static void getZipcodeCSV() throws IOException {
  try {
  /* get zipcode.csv from current working directory  */
  BufferedReader CSVFile = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/zipcode.csv"));

  /* read each row from CSV file and store it in a string */  
  LinkedList<String[]> rows = new LinkedList<String[]>();
  String dataRow = CSVFile.readLine();
   
  /*seperate the columns in each row */
  while ((dataRow = CSVFile.readLine()) != null) 
   rows.addLast(dataRow.split(","));
  
  /* convert the rows and columns into 2D array and put value in zipcodeCSV */
  zipcodeCSV = rows.toArray(new String[rows.size()][]);
  } catch(IOException e) {
  }
 } 
 
 /* get the zipcode for every location and put in locationZipcode hashtable */
 public static void getZipcodeLocations() {
  /* iterate throug globalLocationKeys  */
  for(String location: globalLocationKeys) {
    /* if key is not equal to all sellers  */
   if(!(location.equals("All Sellers"))) {
   
   /* get first to two letters of string which will be the initials of a state, for example: NY*/ 
   String state = location.substring(0, 2);
   
   /* get beginning and end index of city name in string*/
   int cityStart = location.indexOf("-");
   int cityEnd = location.indexOf("-", cityStart + 1);
   
   /* get the name of the city */
   String city = location.substring(cityStart + 2, cityEnd - 1);
 
   /* special case for St.Louis because it is spelled "St.Louis" by shopgoodwill and "Saint Louis" in zipcode.csv*/
   /* change name to match zipcode.csv */
   if(city.equals("St. Louis")) 
    city = "Saint Louis";
   
   /* flag for when zipcode is found*/ 
   boolean notFound = true;
   
   /* iterate through zipcode.csv rows */
   for(int i = 0; i < zipcodeCSV.length; i++) {
    /* if row was no empty */
    if(zipcodeCSV[i].length != 1) {
     /* if zipcode was not found and there is a match*/
     if(notFound && (zipcodeCSV[i][1]).replaceAll("\"", "").equals(city) && (zipcodeCSV[i][2]).replaceAll("\"", "").equals(state)) {
      /* enter location and its zipocode in locationZipcode hashtable*/
       locationZipcode.put(location, (zipcodeCSV[i][0]).replaceAll("\"", ""));    
       /* zipcode was found set flag to false */
       notFound = false;  
      }
     }
    } 
   }
  }
 }

 /*output information about saved auction in txt file in current working directory */
 @SuppressWarnings("unchecked")
 public static void outputSearchLog(File output, Hashtable<String,String> htable) throws IOException {
  try {
   /* create a BufferedWriter to filename that was passed to method*/
   FileWriter fw = new FileWriter(output, false);
   BufferedWriter bw = new BufferedWriter(fw);
   
   /* iterate through hashtable table (searchesHashtable) */
   Set<String> keys = htable.keySet(); 
   for(String key: keys)
     /*write value(search information) to file */
     bw.write(htable.get(key));
   
   /* close stream */
   bw.close();
  } catch(IOException e) {
  }
 }

 public static void main(String args[]) {
  try {
   /* create hashtables for categories and locations*/
   /* keys will be category/location names and values will be their ID numbers*/
   Hashtable<String,String> category = new Hashtable<String,String>();
   Hashtable<String,String> location = new Hashtable<String,String>();

   /* create files to store serialized data for persistent data storage*/
   File serializedCategory = new File("location.ser");
   File serializedLocation = new File("category.ser");
   
   /* create file to out saved auction information */
   File searchLog          = new File("searchlog.txt");

   /* if serialized hashtable (serializedSearches) exists, deserialize it and output its information to searchLog*/
   if(serializedSearches.exists()) {
    searchesHashtable = deserializeHashtable(serializedSearches);
    outputSearchLog(searchLog, searchesHashtable); 
   }
   
   /*if serialized category and location hashtables exist, deserialize and store their data in hashtables  */
   if((serializedCategory).exists() && (serializedLocation).exists()) {
     category = deserializeHashtable(serializedCategory);
     location = deserializeHashtable(serializedLocation);
    }

   /* else use getInformation fill category, location hashtables with data from shopgoodwill */
   /* serialize the hashtables */
   else { 
    GetInformation(category, location);   
    serializeHashtable(category, serializedCategory);
    serializeHashtable(location, serializedLocation);
   }
   
   /* String lists that will hold category/location hashtable keys */
   /* sort keys */
   java.util.List<String> categoryKeys = sortKeys(category);
   java.util.List<String> locationKeys = sortKeys(location);

   /*create files to output list of categories and locations to */
   File outputCategory = new File("categories.txt"); 
   File outputLocation = new File("locations.txt");

   /* output list of categories and locations */
   outputKeysValues(category, categoryKeys, outputCategory); 
   outputKeysValues(location, locationKeys, outputLocation);
   
   /* save html of shopgoodwill search page to current working directory */
   GetSearchHTML();

   /* static string lists now have sorted category/location keys*/
   globalCategoryKeys = categoryKeys;
   globalLocationKeys = locationKeys;
  
   /* static hashtbales are now equal to category/location hashtbales*/
   categoryHashtable = category;
   locationHashtable = location; 

   /* if zipcode.csv file exists in current directory */
   File zipcodeFile = new File(System.getProperty("user.dir")  + "/zipcode.csv");
   if(zipcodeFile.isFile()) {
    /* get static 2D arrays with the same rom/column data values*/
    getZipcodeCSV();

    /* get the zipcodes of all location and put them in static hashtable*/
    getZipcodeLocations();
   }

   /* create instance of Phase3(GUI) and run it */
   EventQueue.invokeLater(new Runnable() {
    @Override
    public void run() {
     Phase3 ex = new Phase3();
    }
   });

  } catch(IOException e) {
  }
 }
}
