package com.danharper.cwk.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

/**
 * Entity representing a Person, or Company
 * @author danharper
 */
@Entity
public class Person implements Serializable
{

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id = null;
   @Version
   @Column(name = "version")
   private int version = 0;

   @Column
   private String email;

   @Column
   private String password;

   @Column
   private String contactName;

   @Column
   private String companyName;

   @Column
   private String profile;

   @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<Idea> ideas = new HashSet<Idea>();

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
         return id.equals(((Person) that).id);
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

   public String getEmail()
   {
      return this.email;
   }

   public void setEmail(final String email)
   {
      this.email = email;
   }

   public String getPassword()
   {
      return this.password;
   }

   public void setPassword(final String password)
   {
      this.password = password;
   }

   public String getContactName()
   {
      return this.contactName;
   }

   public void setContactName(final String contactName)
   {
      this.contactName = contactName;
   }

   public String getCompanyName()
   {
      return this.companyName;
   }

   public void setCompanyName(final String companyName)
   {
      this.companyName = companyName;
   }

   public String getProfile()
   {
      return this.profile;
   }

   public void setProfile(final String profile)
   {
      this.profile = profile;
   }

   @Override
   public String toString()
   {
      return contactName + " - " + companyName;
   }

   public Set<Idea> getIdeas()
   {
      return this.ideas;
   }

   public void setIdeas(final Set<Idea> ideas)
   {
      this.ideas = ideas;
   }
}