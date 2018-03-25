package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.CheckoutRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Checkout;
import com.project.glib.model.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Repository
public class CheckoutDaoImplementation implements ModifyByLibrarian<Checkout> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(CheckoutDaoImplementation.class);
    private final CheckoutRepository checkoutRepository;

    @Autowired
    public CheckoutDaoImplementation(CheckoutRepository checkoutRepository) {
        this.checkoutRepository = checkoutRepository;
    }

    @Override
    public void add(Checkout checkout) throws Exception {
        checkValidParameters(checkout);
        try {
            if (alreadyHasThisCheckout(checkout.getIdDoc(), checkout.getDocType(), checkout.getIdUser()))
                throw new Exception();
            checkoutRepository.save(checkout);
            logger.info("Checkout successfully saved. Checkout details : " + checkout);
        } catch (Exception e) {
            logger.info("Try to add check out with wrong parameters. New check out information : " + checkout);
            throw new Exception("Can't add this check out, some parameters are wrong");
        }
    }

    @Override
    public void update(Checkout checkout) throws Exception {
        checkValidParameters(checkout);
        try {
            checkoutRepository.saveAndFlush(checkout);
            logger.info("Checkout successfully update. Checkout details : " + checkout);
        } catch (Exception e) {
            logger.info("Try to update this check out, check out don't exist or some new check out parameters are wrong. " +
                    "Update check out information : " + checkout);
            throw new Exception("Can't update this check out, check out don't exist or some new check out parameters are wrong");
        }
    }

    @Override
    public void remove(long checkoutId) throws Exception {
        try {
            logger.info("Try to delete check out with check out id = " + checkoutId);
            checkoutRepository.delete(checkoutId);
        } catch (Exception e) {
            logger.info("Try to delete check out with wrong check out id = " + checkoutId);
            throw new Exception("Delete this check out not available, check out don't exist");
        }
    }

    @Override
    public void checkValidParameters(Checkout checkout) throws Exception {
        if (checkout.getIdDoc() <= 0) {
            throw new Exception("Invalid physical document id");
        }

        if (checkout.getIdUser() <= 0) {
            throw new Exception("Invalid user id");
        }

        if (checkout.getCheckoutTime() > System.nanoTime()) {
            throw new Exception("Checkout cannot be in future");
        }

        if (checkout.getReturnTime() <= checkout.getCheckoutTime()) {
            throw new Exception("Return time cannot be less than checkout time");
        }

        if (!Document.isType(checkout.getDocType())) {
            throw new Exception("Invalid document type");
        }

        if (checkout.getShelf().equals("")) {
            throw new Exception("Shelf must exist");
        }
    }

    @Override
    public Checkout getById(long checkoutId) {
        logger.info("Try to get check out with check out id = " + checkoutId);
        return checkoutRepository.findOne(checkoutId);
    }

    @Override
    public long getId(Checkout checkout) throws Exception {
        try {
            return checkoutRepository.findAll().stream()
                    .filter(c -> c.getIdDoc() == checkout.getIdDoc() &&
                            c.getIdUser() == checkout.getIdUser() &&
                            c.getCheckoutTime() == checkout.getCheckoutTime() &&
                            c.getReturnTime() == checkout.getReturnTime() &&
                            c.isRenewed() == checkout.isRenewed() &&
                            c.getDocType().equals(checkout.getDocType()) &&
                            c.getShelf().equals(checkout.getShelf()))
                    .findFirst().get().getId();
        } catch (NoSuchElementException e) {
            throw new Exception("Checkout does not exist");
        }
    }

    public boolean getIsRenewedById(long checkoutId) throws Exception {
        try {
            logger.info("Try to get is renewed check out by check out id = " + checkoutId);
            return checkoutRepository.findOne(checkoutId).isRenewed();
        } catch (Exception e) {
            logger.info("Try to get is renewed check out by wrong check out id = " + checkoutId);
            throw new Exception("Information not available, check out don't exist");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Checkout> getList() {
        try {
            List<Checkout> checkouts = checkoutRepository.findAll();

            for (Checkout checkout : checkouts) {
                logger.info("Checkout list : " + checkout);
            }

            logger.info("Check out list successfully printed");
            return checkouts;
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    public long getNumberOfCheckoutDocumentsByUser(long userId) throws Exception {
        try {
            logger.info("Try to get numbers of check outs documents by user with user id = " + userId);
            return checkoutRepository.findAll().stream()
                    .filter(checkout -> checkout.getIdUser() == userId)
                    .count();
        } catch (Exception e) {
            logger.info("Try to get numbers of check outs documents by user with wrong user id = " + userId);
            throw new Exception("Information not available, user don't exist");
        }
    }

    public List<Checkout> getCheckoutsByUser(long userId) throws Exception {
        try {
            logger.info("Try to get list of check out by user with user id = " + userId);
            return checkoutRepository.findAll().stream()
                    .filter(checkout -> checkout.getIdUser() == userId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.info("Try to get list of check out by user with wrong user id = " + userId);
            throw new Exception("Information not available, user don't exist");
        }
    }

    public boolean alreadyHasThisCheckout(long docId, String docType, long userId) {
        logger.info("Get check has user with id = " + userId +
                " already check out " + docType.toLowerCase() + " with virtual id = " + docId);
        return checkoutRepository.findAll().stream()
                .filter(checkout -> checkout.getIdUser() == userId)
                .filter(checkout -> checkout.getIdDoc() == docId)
                .anyMatch(checkout -> checkout.getDocType().equals(docType));
    }

    public boolean hasRenewedCheckout(long docId, String docType) {
        return checkoutRepository.findAll().stream()
                .filter(checkout -> checkout.getIdDoc() == docId)
                .filter(checkout -> checkout.getDocType().equals(docType))
                .anyMatch(Checkout::isRenewed);
    }
}