package uk.ac.newcastle.enterprisemiddleware.customer;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author AryamanPatronia
 * <p>This is the customer entity.</p>
 * <p>It represents a customer with first and last name, age, email, and phone number.</p>
 */
@Entity
public class Customer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 50)
    private String lastName;

    @NotNull
    @Min(0)  // Ensures the age cannot be negative
    private int age;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 10, max = 15)
    private String phoneNumber;

    /**
     * The getters and setters for the customer fields...
     */
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString()
    {
        return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + ", email="
                + email + ", phoneNumber=" + phoneNumber + "]";
    }
}
