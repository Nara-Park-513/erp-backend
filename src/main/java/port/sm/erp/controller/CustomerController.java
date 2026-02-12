package port.sm.erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.CustomerResponse;
import port.sm.erp.entity.Customer;
import port.sm.erp.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // CORS 설정은 그대로 유지
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // 거래처 목록조회

    @GetMapping("/api/acc/customers")
    public ResponseEntity<Page<CustomerResponse>> getAllCustomers(Pageable pageable) {
        // 1. 서비스에서 페이징 처리된 데이터를 가져옵니다.
        Page<Customer> customers = customerService.getAllCustomers(pageable);

        // 2. 엔티티(Customer)를 응답용 데이터(CustomerResponse)로 변환합니다.
        Page<CustomerResponse> customerResponses = customers.map(CustomerResponse::from);

        return ResponseEntity.ok(customerResponses);
    }

    /*@GetMapping("/api/acc/customers")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();  // Service에서 처리

        if (customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
        }

        List<CustomerResponse> customerResponses = customers.stream()
                .map(CustomerResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(customerResponses);
    }*/

    // 거래처 상세조회
    @GetMapping("/api/acc/customers/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 거래처 등록
    @PostMapping("/api/acc/customers")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    // 거래처 수정
    @PutMapping("/api/acc/customers/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        return updatedCustomer != null ? ResponseEntity.ok(updatedCustomer) : ResponseEntity.notFound().build();
    }

    // 거래처 삭제
    @DeleteMapping("/api/acc/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        return customerService.deleteCustomer(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
