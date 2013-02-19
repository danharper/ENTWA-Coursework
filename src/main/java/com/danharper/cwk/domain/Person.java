package com.danharper.cwk.domain;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import java.lang.Override;
import com.danharper.cwk.domain.Idea;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

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
      String result = getClass().getSimpleName() + " ";
      if (email != null && !email.trim().isEmpty())
         result += "email: " + email;
      if (password != null && !password.trim().isEmpty())
         result += ", password: " + password;
      if (contactName != null && !contactName.trim().isEmpty())
         result += ", contactName: " + contactName;
      if (companyName != null && !companyName.trim().isEmpty())
         result += ", companyName: " + companyName;
      if (profile != null && !profile.trim().isEmpty())
         result += ", profile: " + profile;
      return result;
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