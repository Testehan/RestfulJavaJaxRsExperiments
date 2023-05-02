package com.testehan.openliberty.chpt3;

import com.testehan.openliberty.chpt4.CustomerDatabaseResource;
import com.testehan.openliberty.chpt5.CarResource;
import com.testehan.openliberty.chpt6.Chpt6Service;
import com.testehan.openliberty.chpt7.Chpt7Service;
import com.testehan.openliberty.chpt7.IllegalArgumentExceptionMapper;
import com.testehan.openliberty.chpt9.Chpt9CustomerResource;
import com.testehan.openliberty.chpt9.Chpt9MyService;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ApplicationPath("/services")
public class ShoppingApplication extends Application {
    private Set<Object> singletons = new HashSet<>();
    private Set<Class<?>> instancePerRequest = new HashSet<>();

    public ShoppingApplication() {
        singletons.add(new CustomerResourceService());
        instancePerRequest.add(CustomerDatabaseResource.class);
        instancePerRequest.add(CarResource.class);
        instancePerRequest.add(Chpt6Service.class);
        instancePerRequest.add(Chpt7Service.class);
        instancePerRequest.add(Chpt9CustomerResource.class);
        instancePerRequest.add(Chpt9MyService.class);
        instancePerRequest.add(Cpt10LinkService.class);
        instancePerRequest.add(CustomersService.class);
        instancePerRequest.add(Chpt11CustomerResource.class);
        instancePerRequest.add(Chpt12CustomerResource.class);
        instancePerRequest.add(Chpt13CustomerResource.class);
        instancePerRequest.add(Chpt14Resource.class);
        instancePerRequest.add(Chpt15Resource.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return instancePerRequest;
        // "For our customer service database example, we do not have any per-request services, so our
        // ShoppingApplication.getClasses() method returns an empty set"
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    private Map<String,String> getInitialUsers(){
        Map<String,String> userSecretMap = new HashMap<>();
        userSecretMap.put("dan","password");
        userSecretMap.put("mihai","ciuciu");

        return userSecretMap;
    }
}
