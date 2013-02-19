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

import com.danharper.cwk.domain.Idea;
import com.danharper.cwk.domain.Person;

/**
 * Backing bean for Idea entities.
 * <p>
 * This class provides CRUD functionality for all Idea entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class IdeaBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Idea entities
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

   private Idea idea;

   public Idea getIdea()
   {
      return this.idea;
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
         this.idea = this.example;
      }
      else
      {
         this.idea = findById(getId());
      }
   }

   public Idea findById(Long id)
   {

      return this.entityManager.find(Idea.class, id);
   }

   /*
    * Support updating and deleting Idea entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.idea);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.idea);
            return "view?faces-redirect=true&id=" + this.idea.getId();
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
    * Support searching Idea entities with pagination
    */

   private int page;
   private long count;
   private List<Idea> pageItems;

   private Idea example = new Idea();

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

   public Idea getExample()
   {
      return this.example;
   }

   public void setExample(Idea example)
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
      Root<Idea> root = countCriteria.from(Idea.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Idea> criteria = builder.createQuery(Idea.class);
      root = criteria.from(Idea.class);
      TypedQuery<Idea> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Idea> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String title = this.example.getTitle();
      if (title != null && !"".equals(title))
      {
         predicatesList.add(builder.like(root.<String> get("title"), '%' + title + '%'));
      }
      String details = this.example.getDetails();
      if (details != null && !"".equals(details))
      {
         predicatesList.add(builder.like(root.<String> get("details"), '%' + details + '%'));
      }
      Person person = this.example.getPerson();
      if (person != null)
      {
         predicatesList.add(builder.equal(root.get("person"), person));
      }
      int stateType = this.example.getStateType();
      if (stateType != 0)
      {
         predicatesList.add(builder.equal(root.get("stateType"), stateType));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Idea> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Idea entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Idea> getAll()
   {

      CriteriaQuery<Idea> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Idea.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Idea.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final IdeaBean ejbProxy = this.sessionContext.getBusinessObject(IdeaBean.class);

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

            return String.valueOf(((Idea) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Idea add = new Idea();

   public Idea getAdd()
   {
      return this.add;
   }

   public Idea getAdded()
   {
      Idea added = this.add;
      this.add = new Idea();
      return added;
   }
}