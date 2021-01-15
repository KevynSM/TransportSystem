package D3.controllers;

import D3.models.*;

import java.util.LinkedList;
import java.util.List;

public class TServiceClass implements TService{
    List<Client> clients = new LinkedList<Client>();
    List<Employee> employees = new LinkedList<Employee>();
    List<Local> locals = new LinkedList<Local>();



    @Override
    public boolean category_existent(String category) {
        return category.equals("Condutor") ||
                category.equals("Carregador") ||
                category.equals("Gestor");
    }

    @Override
    public boolean permission_existent(String category) {
        return category.equals('N') || category.equals('S') ||  category.equals('P');
    }

    @Override
    public boolean has_employee_in_category(String name, String category) {
        for( Employee atual_employee : employees){
            if(atual_employee.get_name()==name){
                if (category.equals("Condutor") && atual_employee instanceof Driver) {
                    return true;
                }
                else if (category.equals("Carregador") && atual_employee instanceof Loader) {
                    return true;
                }
                else if (category.equals("Gestor") && atual_employee instanceof Manager) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int register_employee(String category, String permission, String name) {
        Employee newEmployee;
        if (category.equals("Condutor")) {
            newEmployee = new DriverClass(name, permission);
            this.employees.add(newEmployee);
        }
        else if (category.equals("Carregador")) {
            newEmployee = new LoaderClass(name, permission);
            this.employees.add(newEmployee);
        }
        else if (category.equals("Gestor")) {
            newEmployee = new ManagerClass(name, permission);
            this.employees.add(newEmployee);
        }
        return this.employees.size();
    }

    @Override
    public boolean has_client(String name) {
        for(Client client : clients){
            if(client.get_name().equals(name)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean has_idClient(int idClient) {
        return clients.size() >= idClient;
    }

    @Override
    public int register_client(String name, int idEmployee) {
        Client newClient = new ClientClass(name, idEmployee);
        this.clients.add(newClient);
        return this.clients.size();
    }

    @Override
    public int[] register_item(String nameClient, String nameItem, String[] permissions) {
        for(int i=0; i<clients.size();i++){
            if(clients.get(i).get_name().equals(nameClient)){
                List<Item> inventory = clients.get(i).get_inventory();
                Item newItem = new ItemClass(nameItem, permissions);
                inventory.add(newItem);
                int[] ids = new int[0]; ids[0]= i; ids[i] = inventory.size();
                return ids;
            }
        }
        return null;
    }

    @Override
    public boolean has_local(String nameLocal) {
        for(Local local: this.locals) {
            if(local.getName().equals(nameLocal)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean has_idLocal(int idLocal) {
        return this.locals.size() >= idLocal;
    }

    @Override
    public int register_local(String nameLocal) {
        Local newLocal = new LocalClass(nameLocal);
        this.locals.add(newLocal);
        return this.locals.size();
    }

    @Override
    public boolean has_idItem(int idClient, int idItem) {
        return this.clients.get(idClient).get_inventory().size() >= idItem;
    }

    @Override
    public boolean has_employee(int idEmployee) {
        return employees.size() >= idEmployee;
    }

    @Override
    public boolean readFile(String nameFile) {
        return false;
    }

    @Override
    public void saveFile(String nameFile) {

    }

    @Override
    public boolean has_items(int idClient, String[][] items) {
        Client atual_client = clients.get(idClient - 1);
        int tam_inventory = atual_client.get_inventory().size();
        for(int i=0; i<items.length; i++){
            int idItem = Integer.parseInt(items[i][1]);
            if(! (idItem <= tam_inventory) ){
               return false;
            }
        }
        return true;

    }

    @Override
    public boolean has_employees(String[] idEmployees) {
        for(String id : idEmployees){
            if(!has_employee(Integer.parseInt(id))){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean driver_have_permission(String idEmployee, String[][] items) {
        Employee atualEmployee = employees.get( Integer.parseInt(idEmployee)-1 );
        for(int i=0; i<items.length; i++){
            List<String> permissions = atualEmployee.getPermissions();
            if(! permissions.contains(items[i][0]) ){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean loaders_have_permissions(String[] idEmployees, String[][] items) {
        return false;
    }

    @Override
    public String register_deposit(int idClient, int idLocal, String[] idEmployees, String[][] items) {
        return null;
    }

    @Override
    public boolean have_quant_items(int idClient, String[][] items) {
        Client atual_client = clients.get(idClient - 1);
        List<Item> inventory = atual_client.get_inventory();
        for(int i=0; i<items.length; i++){
            int quantItem = Integer.parseInt(items[i][2]);
            int codItem = Integer.parseInt(items[i][1]);
            int quantity = inventory.get(codItem).get_quantity();
            if( quantItem <= quantity ){
                return false;
            }
        }
        return true;
    }

    @Override
    public String register_delivery(int idClient, int idLocal, String[] idEmployees, String[][] items) {
        return null;
    }

    @Override
    public boolean has_item_client(int idClient, int idItem) {
        List<Item> inventory = clients.get(idClient-1).get_inventory();
        return inventory.size() >= idItem;
    }

    @Override
    public boolean has_delivery_client(int idClient, int idDelivery) {
        List<Delivery> deliveries = clients.get(idClient-1).get_delivery();
        return deliveries.size() >= idDelivery;
    }

    @Override
    public List<String> info_delivery(int idClient, int idDelivery) {
        Delivery delivery = this.clients.get(idClient - 1).getDelivery(idDelivery);
        List<String> stringList = new LinkedList<String>();

        String localName = this.locals.get(delivery.getIdLocal()).getName();
        String driverInfo = "" + delivery.getDriver().getPermissions() + " " + delivery.getDriver().get_name();

        stringList.add(localName);
        stringList.add(driverInfo);

        List<Loader> listLoader = delivery.getLoaders();
        for(Loader loader: listLoader) {
            stringList.add("" + loader.getPermissions() + " " + loader.get_name());
        }

        List<Item> itemList = delivery.getItems();
        for(Item item: itemList) {
            int identificarItem = this.clients.get(idClient).get_inventory().indexOf(item) + 1;
            stringList.add("" + identificarItem + " " + item.get_quantity() + " " + item.getName());
        }


        return stringList;
    }

    @Override
    public String get_nameEmployee(int idEmployee) {
        return this.employees.get(idEmployee).get_name();
    }

    @Override
    public String get_categoryEmployee(int idEmployee) {
        if (this.employees.get(idEmployee) instanceof Driver) {
            return "Condutor";
        }
        else if (this.employees.get(idEmployee) instanceof Loader) {
            return "Carregador";
        }
        else if (this.employees.get(idEmployee) instanceof Manager) {
            return "Gestor";
        }
    }

    @Override
    public String get_permissionEmployee(int idEmployee) {
        return null;
    }

    @Override
    public String[] info_depositsE(int idEmployee) {
        return new String[0];
    }

    @Override
    public String[] info_deliveriesE(int idEmployee) {
        return new String[0];
    }

    @Override
    public boolean isManager(int idEmployee) {
        return this.employees.get(idEmployee - 1) instanceof ManagerClass; 
    }

    @Override
    public String get_nameClient(int idClient) {
        return this.clients.get(idClient).get_name();
    }

    @Override
    public String get_nameManager(int idClient) {
        int idManager =  this.clients.get(idClient).getIdManager();
        return this.employees.get(idManager).get_name();
    }

    @Override
    public List<String> info_itens(int idClient) {
        List<String> strList = new LinkedList<String>();
        for(int i = 0; i < this.clients.get(idClient).get_inventory().size(); i++) {
            strList.add(this.info_item(idClient,i + 1 ));
        }
        return strList;
    }

    @Override
    public String[] info_deposits(int idClient) {
        return new String[0];
    }

    @Override
    public String[] info_deliveries(int idClient) {
        return new String[0];
    }

    @Override
    public String info_item(int idClient, int idItem) {
        Item item = this.clients.get(idClient).get_inventory().get(idItem);
        String permissions = String.join(", ", item.getPermissions());
        String str = "" + item.get_quantity() + " " + permissions + " " + item.getName();
        return str;
    }
}
