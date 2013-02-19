package com.danharper.cwk.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.danharper.cwk.domain.Person;

/**
 * Backing bean for Person entities.
 * <p>
 * This class provides CRUD functionality for all Person entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PersonBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Person entities
    */

   private Long id;

   public Long getId()
   {
      return this.id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   private Person person;

   public Person getPerson()
   {
      return this.person;
   }

   @Inject
   private Conversation conversation;

   @PersistenceContext(type = PersistenceContextType.EXTENDED)
   private EntityManager entityManager;

   public String create()
   {

      this.conversation.begin();
      return "create?faces-redirect=true";
   }

   public void retrieve()
   {

      if (FacesContext.getCurrentInstance().isPostback())
      {
         return;
      }

      if (this.conversation.isTransient())
      {
         this.conversation.begin();
      }

      if (this.id == null)
      {
         this.person = this.example;
      }
      else
      {
         this.person = findById(getId());
      }
   }

   public Person findById(Long id)
   {

      return this.entityManager.find(Person.class, id);
   }

   /*
    * Support updating and deleting Person entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.person);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.person);
            return "view?faces-redirect=true&id=" + this.person.getId();
         }
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   public String delete()
   {
      this.conversation.end();

      try
      {
         this.entityManager.remove(findById(getId()));
         this.entityManager.flush();
         return "search?faces-redirect=true";
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   /*
    * Support searching Person entities with pagination
    */

   private int page;
   private long count;
   private List<Person> pageItems;

   private Person example = new Person();

   public int getPage()
   {
      return this.page;
   }

   public void setPage(int page)
   {
      this.page = page;
   }

   public int getPageSize()
   {
      return 10;
   }

   public Person getExample()
   {
      return this.example;
   }

   public void setExample(Person example)
   {
      this.example = example;
   }

   public void search()
   {
      this.page = 0;
   }

   public void paginate()
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

      // Populate this.count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<Person> root = countCriteria.from(Person.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Person> criteria = builder.createQuery(Person.class);
      root = criteria.from(Person.class);
      TypedQuery<Person> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Person> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String email = this.example.getEmail();
      if (email != null && !"".equals(email))
      {
         predicatesList.add(builder.like(root.<String> get("email"), '%' + email + '%'));
      }
      String password = this.example.getPassword();
      if (password != null && !"".equals(password))
      {
         predicatesList.add(builder.like(root.<String> get("password"), '%' + password + '%'));
      }
      String contactName = this.example.getContactName();
      if (contactName != null && !"".equals(contactName))
      {
         predicatesList.add(builder.like(root.<String> get("contactName"), '%' + contactName + '%'));
      }
      String companyName = this.example.getCompanyName();
      if (companyName != null && !"".equals(companyName))
      {
         predicatesList.add(builder.like(root.<String> get("companyName"), '%' + companyName + '%'));
      }
      String profile = this.example.getProfile();
      if (profile != null && !"".equals(profile))
      {
         predicatesList.add(builder.like(root.<String> get("profile"), '%' + profile + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Person> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Person entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Person> getAll()
   {

      CriteriaQuery<Person> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Person.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Person.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final PersonBean ejbProxy = this.sessionContext.getBusinessObject(PersonBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context,
               UIComponent component, String value)
         {

            return ejbProxy.findById(Long.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context,
               UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((Person) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Person add = new Person();

   public Person getAdd()
   {
      return this.add;
   }

   public Person getAdded()
   {
      Person added = this.add;
      this.add = new Person();
      return added;
   }
}