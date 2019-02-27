package com.adsi.view;

import com.adsi.modelo.MicroMarket;
import com.adsi.view.util.JsfUtil;
import com.adsi.view.util.JsfUtil.PersistAction;
import com.adsi.controller.MicroMarketFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("microMarketController")
@SessionScoped
public class MicroMarketController implements Serializable {

    @EJB
    private com.adsi.controller.MicroMarketFacade ejbFacade;
    private List<MicroMarket> items = null;
    private MicroMarket selected;

    public MicroMarketController() {
    }

    public MicroMarket getSelected() {
        return selected;
    }

    public void setSelected(MicroMarket selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MicroMarketFacade getFacade() {
        return ejbFacade;
    }

    public MicroMarket prepareCreate() {
        selected = new MicroMarket();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/eng").getString("MicroMarketCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/eng").getString("MicroMarketUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/eng").getString("MicroMarketDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<MicroMarket> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/eng").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/eng").getString("PersistenceErrorOccured"));
            }
        }
    }

    public MicroMarket getMicroMarket(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<MicroMarket> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<MicroMarket> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = MicroMarket.class)
    public static class MicroMarketControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MicroMarketController controller = (MicroMarketController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "microMarketController");
            return controller.getMicroMarket(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof MicroMarket) {
                MicroMarket o = (MicroMarket) object;
                return getStringKey(o.getZipCode());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), MicroMarket.class.getName()});
                return null;
            }
        }

    }

}
