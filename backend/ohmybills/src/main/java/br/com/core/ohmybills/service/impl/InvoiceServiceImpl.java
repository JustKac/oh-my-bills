package br.com.core.ohmybills.service.impl;

import java.util.UUID;

import br.com.core.ohmybills.service.InvoiceService;
import org.springframework.stereotype.Service;

import br.com.core.ohmybills.model.Invoice;
import br.com.core.ohmybills.repository.InvoiceRepository;

@Service
public class InvoiceServiceImpl extends GenericServiceImpl<Invoice, UUID, InvoiceRepository> implements InvoiceService {

    public InvoiceServiceImpl(InvoiceRepository repository) {
        super(repository);
    }

}
