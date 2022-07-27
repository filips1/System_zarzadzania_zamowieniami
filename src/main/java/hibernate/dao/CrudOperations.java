package hibernate.dao;

import hibernate.util.HibernateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CrudOperations<C> {

    SessionFactory factory;
    Session session;


    public CrudOperations(){
        factory = HibernateUtils.getSessionFactory();
        session = factory.openSession();

    }
    private void Operation(Runnable fun){
        session.beginTransaction();
        fun.run();
        session.getTransaction().commit();

    }
    public void create(C object){
        Operation(()->{
            session.save(object);
            System.out.println("Inserted Successfully");
        });
    }

    //example of use: productService.where(x-> x.getPrice().equals(25),Products.class);
    public List<C> where(Function<C,Boolean> function, Class<C> type){
        List<C> data = session.createQuery("FROM "+type.getName()).list();
        List<C> newList = new ArrayList<>();
        for(C element:data){
            if(function.apply(element)){
                System.out.println(element);
                newList.add(element);
            }
        }
        return newList;
    }

    public C single(Function<C,Boolean> function, Class<C> type){
        List<C> data = session.createQuery("FROM "+type.getName()).list();
        C object = null;

        for(C element:data){
            if(function.apply(element)){
                object = element;
                break;
            }
        }
        return object;
    }
    public void delete(C object){
        Operation(()->{
            session.delete(object);
            System.out.println("Deleted Successfully");
        });
    }
    public void update(C object){
        Operation(()->{
            session.update(object);

            System.out.println("Updated Successfully");
        });
    }

    @Deprecated
    public List get(String a) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List lista = session.createQuery("FROM "+a).list();
            tx.commit();

            return lista;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
        return null;
    }

    @Deprecated
    public List<C> getItemsUsingCriteriaBuilder(Class<C> type) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<C> criteria = builder.createQuery(type);
        criteria.from(type);
        List<C> data = session.createQuery(criteria).getResultList();

        return data;
    }
    public List<C> getItems(Class<C> type){
        List<C> data = session.createQuery("FROM "+type.getName()).list();
        return data;
    }
    public C getItem(Class<C> typeParameterClass,Integer id){
        C object = (C)session.get(typeParameterClass,id);
        return object;
    }
    public void stop(){
        if (session != null) {
            session.close();
        }
    }
}
