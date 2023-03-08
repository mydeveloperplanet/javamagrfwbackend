package com.mydeveloperplanet.javamagrfwbackend;

import java.util.HashMap;
import java.util.List;
import java.util.stream.StreamSupport;

import com.mydeveloperplanet.javamagrfwbackend.api.CustomerApi;
import com.mydeveloperplanet.javamagrfwbackend.model.Customer;
import com.mydeveloperplanet.javamagrfwbackend.model.CustomerFullData;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController implements CustomerApi {

    private final HashMap<Long, com.mydeveloperplanet.javamagrfwbackend.Customer> customers = new HashMap<>();
    private Long index = 0L;

    @Override
    public ResponseEntity<CustomerFullData> createCustomer(Customer apiCustomer) {
        com.mydeveloperplanet.javamagrfwbackend.Customer customer = new com.mydeveloperplanet.javamagrfwbackend.Customer();
        customer.setCustomerId(index);
        customer.setFirstName(apiCustomer.getFirstName());
        customer.setLastName(apiCustomer.getLastName());
        customer.setEmail((apiCustomer.getEmail()));

        customers.put(index, customer);
        index++;

        return ResponseEntity.ok(domainToApi(customer));
    }

    @Override
    public ResponseEntity<CustomerFullData> deleteCustomer(@PathVariable("customerId") Long customerId) {
        if (customers.containsKey(customerId)) {
            com.mydeveloperplanet.javamagrfwbackend.Customer customer = customers.get(customerId);
            customers.remove(customerId);
            return ResponseEntity.ok(domainToApi(customer));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<CustomerFullData> getCustomer(Long customerId) {
        if (customers.containsKey(customerId)) {
            return ResponseEntity.ok(domainToApi(customers.get(customerId)));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<List<CustomerFullData>> getCustomers() {
        return ResponseEntity.ok(StreamSupport.stream(customers.values().spliterator(), false).map(this::domainToApi).toList());

    }

    private CustomerFullData domainToApi(com.mydeveloperplanet.javamagrfwbackend.Customer customer) {
        CustomerFullData cfd = new CustomerFullData();
        cfd.setCustomerId(customer.getCustomerId());
        cfd.setFirstName(customer.getFirstName());
        cfd.setLastName(customer.getLastName());
        cfd.setEmail((customer.getEmail()));
        return cfd;
    }
}
