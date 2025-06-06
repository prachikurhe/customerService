package com.waterjar.customerservice.service;

import com.waterjar.customerservice.dto.CustomerDTO;
import com.waterjar.customerservice.entity.Customer;
import com.waterjar.customerservice.exception.ResourceNotFoundException;
import com.waterjar.customerservice.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CustomerDTO createCustomer(CustomerDTO dto) {
        Customer entity = modelMapper.map(dto, Customer.class);
        Customer saved = repo.save(entity);
        return modelMapper.map(saved, CustomerDTO.class);
    }

    @Override
    public CustomerDTO getCustomer(Long id) {
        Customer customer = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return modelMapper.map(customer, CustomerDTO.class);
    }


    @Override
    public List<CustomerDTO> getAllCustomers() {
        return repo.findAll().stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        Customer existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        modelMapper.map(dto, existing); // only updates matching fields

        Customer updated = repo.save(existing);
        return modelMapper.map(updated, CustomerDTO.class);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        repo.delete(existing);
    }
}
