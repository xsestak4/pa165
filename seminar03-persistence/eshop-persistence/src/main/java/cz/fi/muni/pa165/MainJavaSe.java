package cz.fi.muni.pa165;

import java.sql.SQLException;
import java.util.List;
import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cz.fi.muni.pa165.entity.Category;
import cz.fi.muni.pa165.entity.Product;
import cz.fi.muni.pa165.enums.Color;
import javax.persistence.PersistenceException;

public class MainJavaSe {
	private static EntityManagerFactory emf;

	public static void main(String[] args) throws SQLException {
		// The following line is here just to start up a in-memory database
		AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(InMemoryDatabaseSpring.class);

		emf = Persistence.createEntityManagerFactory("default");
		try {
			// BEGIN YOUR CODE
			//task04();
                        //task05();
                        //task06();
                        task08();
			// END YOUR CODE
		} finally {
			emf.close();
			appContext.close();
		}
	}

	private static void task04() {
		// TODO under this line, persist two categories, one with name
		// Electronics and second with name Musical
		// You must first obtain the Entity manager
		// Then you have to start transaction using getTransaction().begin()
		// Then use persist() to persist both of the categories and finally commit the transaction
                EntityManager emWork = emf.createEntityManager();
		emWork.getTransaction().begin();
                
		Category c1 = new Category();
		c1.setName("Electronics");
                
		Category c2=new Category();
		c2.setName("Musical");
                
		emWork.persist(c1);
		emWork.persist(c2);
		
		emWork.getTransaction().commit();
		

// The code below is just testing code. Do not modify it
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		List<Category> categories = em.createQuery(
				"select c from Category c order by c.name", Category.class)
				.getResultList();

                if (categories.size() != 2) 
                    throw new RuntimeException("Expected two categories!");

		assertEq(categories.get(0).getName(), "Electronics");
		assertEq(categories.get(1).getName(), "Musical");

		em.getTransaction().commit();
		em.close();

		System.out.println("Succesfully found Electronics and Musical!");
	}

	private static void task05() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Category category = new Category();
		category.setName("Electronics");
		em.persist(category);
		em.getTransaction().commit();
		em.close();

		// TODO under this line. create new EM and start new transaction. Merge
		// the detached category
		// into the context and change the name to "Electro"
                EntityManager em2 = emf.createEntityManager();
		em2.getTransaction().begin();
                
		category = em2.find(Category.class, category.getId());
		category.setName("Electro");
                
                em2.merge(category);
		em2.getTransaction().commit();
		em2.close();

		// The code below is just testing code. Do not modify it
		EntityManager checkingEm = emf.createEntityManager();
		checkingEm.getTransaction().begin();
		Category cat = checkingEm.find(Category.class, category.getId());
		assertEq(cat.getName(), "Electro");
		System.out.println("Name changed successfully to Electro");
		checkingEm.getTransaction().commit();
		checkingEm.close();
	}

	private static void task06() {
		// TODO Map class Product to be an entity with the following attributes:
		// * id - autogenerated @Id using IDENTITY column
		// * name - nonnullable, unique
		// * color - you will have to create new ENUM for this
		// * java.util.Date addedDate - this field should contain only date in the database. Use @Temporal annotation
		// Then persist exactly one Product with the following values:
		// * name='Guitar'
		// * color=Color.BLACK
		// * dateAdded = 20-01-2011 - to fill java.util.Date use Calendar 
		//
		// Additional task: Change the underlying table of Product entity to be ESHOP_PRODUCTS. After you do this, check this by inspecting console output (the CREATE TABLE statement)
		//
		// To test your code uncomment the commented code at the end of this method.
                EntityManager e = emf.createEntityManager();
		e.getTransaction().begin();
                
                Product prod = new Product();
		prod.setName("Guitar");
		prod.setColor(Color.BLACK);
                
                Calendar cal1 = Calendar.getInstance();
		cal1.set(Calendar.YEAR, 2011);
		cal1.set(Calendar.MONTH, 0);
		cal1.set(Calendar.DAY_OF_MONTH, 20);
                
		prod.setAddedDate(cal1.getTime());
		
                
		
		e.persist(prod);
		e.getTransaction().commit();
		e.close();
                

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Product p = em.createQuery("select p from Product p", Product.class)
				.getSingleResult();
		em.getTransaction().commit();
		em.close();

	/** TODO Uncomment the following test code after you are finished!*/
	 
		assertEq(p.getName(), "Guitar");
		Calendar cal = Calendar.getInstance();
		cal.setTime(p.getAddedDate());
		assertEq(cal.get(Calendar.YEAR), 2011);
		assertEq(cal.get(Calendar.MONTH), 0);
		assertEq(cal.get(Calendar.DAY_OF_MONTH), 20);
		assertEq(cal.get(Calendar.MINUTE), 0);
		assertEq(p.getColor(), Color.BLACK);
		System.out
				.println("Found Guitar with correct values. Starting uniqueness test.");

		em = emf.createEntityManager();
		em.getTransaction().begin();
		Product p2 = new Product();
		p2.setName("Guitar");
		Product p3 = new Product();
		p3.setName("Violin");
		em.persist(p3);
		System.out.println("Successfully persited Violin");
		try {
			em.persist(p2);
			
			throw new RuntimeException(
					"Successfully saved new Product with the same name (Guitar) it should be unique!");
		} catch (PersistenceException ex) {
			System.out
					.println("Unsuccessfully saved second object with name Guitar -> OK");
		}
		em.close();
	

		System.out.println("Task6 ok!");
		
	}
	
	private static void task08() {
		System.out.println("Running TASK 08");
		//Implement business equivalence on Product (equals and hashcode method). Tip: Product.name is nonullable and should have unique values 
		//This is very important concept and you should understand it beyond just "making this method work"
		// see https://developer.jboss.org/wiki/EqualsandHashCode
		
		//TODO after you implement equals nad hashCode, you can uncomment the code below. It will try
		// to check whether you are doing everything correctly. 
	
/* TODO uncomment the following (it should work if you were successfull with task08)*/


		class MockProduct extends Product {
			private boolean getNameCalled = false;
			@Override
			public String getName() {
				getNameCalled = true;
				return super.getName();
			}
		}
		
		Product p = new Product();
		p.setName("X");
		p.setId(2l);
		Product p2 = new Product();
		p2.setName("X");
		p2.setId(4l);
		MockProduct mp = new MockProduct();
		mp.setName("X");
		p.setId(3l);
		
		System.out.println("Your equals and hashcode should work on unique 'name' attribute");
		if (p.equals(p2) && p.hashCode()==p2.hashCode()){
			System.out.println("CORRECT");
		} else System.out.println("INCORRECT!");
		
		
		System.out.println("Your equals should use instanceof and not getClass()==");
		if (p.equals(mp)){
			System.out.println("CORRECT");
		} else
			System.out.println("INCORRECT!");

		System.out.println("Your equals should call getter to get 'name' value on the other object, because other object may be a proxy class instance");
		if (mp.getNameCalled){
			System.out.println("CORRECT");
		} else System.out.println("INCORRECT!");
		 
	
	}

	private static void assertEq(Object obj1, Object obj2) {
		if (!obj1.equals(obj2)) {
			throw new RuntimeException(
					"Expected these two objects to be identical: " + obj1
							+ ", " + obj2);
		} else {
			System.out.println("OK objects are identical");
		}
	}

}
