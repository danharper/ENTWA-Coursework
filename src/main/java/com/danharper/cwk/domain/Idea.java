package com.danharper.cwk.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * Entity representing an Idea
 * @author danharper
 */
@Entity
public class Idea implements Serializable
{

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id = null;
   @Version
   @Column(name = "version")
   private int version = 0;

   @Column
   private String title;

   @Column
   private String details;

   @Temporal(TemporalType.TIMESTAMP)
   private Date createdAt;

   @ManyToMany
   private Set<Area> areas = new HashSet<Area>();

   @ManyToOne
   private Person person;

   @Column
   private int stateType;
   
   @Transient
   private static Map<Integer, String> stateMap = new HashMap<Integer, String>()
   {{
      put(1, "Pending");
      put(2, "Active");
      put(3, "Assigned");
      put(4, "Withdrawn");
   }};
   
   public Idea()
   {
   }

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }

   @Override
   public boolean equals(Object that)
   {
      if (this == that)
      {
         return true;
      }
      if (that == null)
      {
         return false;
      }
      if (getClass() != that.getClass())
      {
         return false;
      }
      if (id != null)
      {
         return id.equals(((Idea) that).id);
      }
      return super.equals(that);
   }

   @Override
   public int hashCode()
   {
      if (id != null)
      {
         return id.hashCode();
      }
      return super.hashCode();
   }

   public String getTitle()
   {
      return this.title;
   }

   public void setTitle(final String title)
   {
      this.title = title;
   }

   public String getDetails()
   {
      return this.details;
   }

   public void setDetails(final String details)
   {
      this.details = details;
   }

   public Date getCreatedAt()
   {
      return this.createdAt;
   }

   public void setCreatedAt(final Date createdAt)
   {
      this.createdAt = createdAt;
   }

   public Set<Area> getAreas()
   {
      return this.areas;
   }

   public void setAreas(final Set<Area> areas)
   {
      this.areas = areas;
   }

   public Person getPerson()
   {
      return this.person;
   }

   public void setPerson(final Person person)
   {
      this.person = person;
   }

   public int getStateType()
   {
      return this.stateType;
   }

   public void setStateType(final int stateType)
   {
      this.stateType = stateType;
   }
   
   /**
    * Retrieve a human readable version of the Idea's "State"
    * eg. Pending, Active, Assigned and Withdrawn
    * @return The state
    */
   public String getState()
   {
       return stateMap.get(this.stateType);
   }

   @Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      if (title != null && !title.trim().isEmpty())
         result += "title: " + title;
      if (details != null && !details.trim().isEmpty())
         result += ", details: " + details;
      result += ", stateType: " + stateType;
      return result;
   }
}