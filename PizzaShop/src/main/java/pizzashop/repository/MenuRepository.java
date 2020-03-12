package pizzashop.repository;

import pizzashop.model.MenuDataModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

@SuppressWarnings({"java:S1106","java:S1120","java:S1108","java:S1943"})
public class MenuRepository {
    private static String filename = "data/menu.txt";
    private static final Logger LOGGER=Logger.getLogger(MenuRepository.class);

    private List<MenuDataModel> listMenu;

    public MenuRepository(){
        //Implicit constructor
    }

    private void readMenu(){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());
        this.listMenu= new ArrayList();
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = null;
            while((line=br.readLine())!=null){
                MenuDataModel menuItem=getMenuItem(line);
                listMenu.add(menuItem);
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    private static MenuDataModel getMenuItem(String line){
        if (line==null|| "".equals(line)){
            return null;
        }
        StringTokenizer st=new StringTokenizer(line, ",");
        String name= st.nextToken();
        double price = Double.parseDouble(st.nextToken());
        MenuDataModel item=null;
        item = new MenuDataModel(name, 0, price);
        return item;
    }

    public List<MenuDataModel> getMenu(){
        //create a new menu for each table, on request
        readMenu();
        return new ArrayList<>(listMenu);
    }

}
