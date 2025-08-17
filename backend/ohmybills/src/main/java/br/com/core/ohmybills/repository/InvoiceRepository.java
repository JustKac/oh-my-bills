package br.com.core.ohmybills.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.core.ohmybills.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

}
