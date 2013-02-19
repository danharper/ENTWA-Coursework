package com.danharper.cwk.domain;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import java.lang.Override;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Set;
import java.util.HashSet;
import com.danharper.cwk.domain.Area;
import javax.persistence.ManyToMany;
import com.danharper.cwk.domain.User;
import javax.persistence.ManyToOne;

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

   @Column
   private int state;

   @Temporal(TemporalType.TIMESTAMP)
   private Date createdAt;

   @ManyToMany
   private Set<Area> areas = new HashSet<Area>();

   @ManyToOne
   private User user;

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

   public int getState()
   {
      return this.state;
   }

   public void setState(final int state)
   {
      this.state = state;
   }

   public Date getCreatedAt()
   {
      return this.createdAt;
   }

   public void setCreatedAt(final Date createdAt)
   {
      this.createdAt = createdAt;
   }

   @Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      if (title != null && !title.trim().isEmpty())
         result += "title: " + title;
      if (details != null && !details.trim().isEmpty())
         result += ", details: " + details;
      result += ", state: " + state;
      return result;
   }

   public Set<Area> getAreas()
   {
      return this.areas;
   }

   public void setAreas(final Set<Area> areas)
   {
      this.areas = areas;
   }

   public User getUser()
   {
      return this.user;
   }

   public void setUser(final User user)
   {
      this.user = user;
   }
}