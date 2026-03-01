package port.sm.erp.controller

import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import port.sm.erp.dto.CustomerResponse
import port.sm.erp.entity.Customer
import port.sm.erp.service.CustomerService
import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collectors

@RestController
@CrossOrigin(origins = ["http://localhost:5173"]) // CORS 설정은 그대로 유지
@RequiredArgsConstructor
class CustomerController {
    private val customerService: CustomerService? = null

    @get:GetMapping("/api/acc/customers")
    val allCustomers: ResponseEntity<MutableList<CustomerResponse?>?>
        // 거래처 목록조회
        get() {
            val customers = customerService!!.getAllCustomers() // Service에서 처리

            if (customers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body<MutableList<CustomerResponse?>?>(mutableListOf<CustomerResponse?>())
            }

            val customerResponses = customers.stream()
                .map<CustomerResponse?> { customer: Customer? -> CustomerResponse.from(customer) }
                .collect(Collectors.toList())

            return ResponseEntity.ok<MutableList<CustomerResponse?>?>(customerResponses)
        }

    // 거래처 상세조회
    @GetMapping("/api/acc/customers/{id}")
    fun getCustomer(@PathVariable id: Long?): ResponseEntity<Customer?> {
        val customer = customerService!!.getCustomerById(id)
        return customer.map<ResponseEntity<Customer?>?>(Function { body: Customer? -> ResponseEntity.ok(body) })
            .orElseGet(Supplier { ResponseEntity.notFound().build<Customer?>() })
    }

    // 거래처 등록
    @PostMapping("/api/acc/customers")
    fun createCustomer(@RequestBody customer: Customer?): ResponseEntity<Customer?> {
        val createdCustomer = customerService!!.createCustomer(customer)
        return ResponseEntity.status(HttpStatus.CREATED).body<Customer?>(createdCustomer)
    }

    // 거래처 수정
    @PutMapping("/api/acc/customers/{id}")
    fun updateCustomer(@PathVariable id: Long?, @RequestBody customer: Customer?): ResponseEntity<Customer?> {
        val updatedCustomer = customerService!!.updateCustomer(id, customer)
        return if (updatedCustomer != null) ResponseEntity.ok<Customer?>(updatedCustomer) else ResponseEntity.notFound()
            .build<Customer?>()
    }

    // 거래처 삭제
    @DeleteMapping("/api/acc/customers/{id}")
    fun deleteCustomer(@PathVariable id: Long?): ResponseEntity<Void?> {
        return if (customerService!!.deleteCustomer(id)) ResponseEntity.noContent()
            .build<Void?>() else ResponseEntity.notFound().build<Void?>()
    }
}