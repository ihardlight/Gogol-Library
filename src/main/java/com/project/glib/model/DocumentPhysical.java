package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "documents_physical")
public class DocumentPhysical {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "shelf")
    private String shelf;

    @Column(name = "can_booked")
    private boolean canBooked;

    @Column(name = "is_reference")
    private boolean isReference;

    @Column(name = "id_doc")
    private long idDoc;

    @Column(name = "doc_type")
    private String docType;

    public DocumentPhysical() {
    }

    public DocumentPhysical(String shelf, boolean canBooked, boolean isReference, long idDoc, String docType) {
        this.shelf = shelf;
        this.canBooked = canBooked;
        this.isReference = isReference;
        this.idDoc = idDoc;
        this.docType = docType;
    }

    @Override
    public String toString() {
        return "DocumentPhysical{" +
                "id=" + id +
                ", shelf='" + shelf + '\'' +
                ", canBooked=" + canBooked +
                ", isReference=" + isReference +
                ", idDoc=" + idDoc +
                ", docType='" + docType + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public boolean isCanBooked() {
        return canBooked;
    }

    public void setCanBooked(boolean canBooked) {
        this.canBooked = canBooked;
    }

    public boolean isReference() {
        return isReference;
    }

    public void setReference(boolean reference) {
        isReference = reference;
    }

    public long getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(long idDoc) {
        this.idDoc = idDoc;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }
}