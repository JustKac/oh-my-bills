package br.com.core.ohmybills.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.core.ohmybills.model.Invoice;
import br.com.core.ohmybills.repository.InvoiceRepository;

@Service
public class InvoiceService extends GenericServiceImpl<Invoice, UUID, InvoiceRepository> {

    public InvoiceService(InvoiceRepository repository) {
        super(repository);
    }

}
