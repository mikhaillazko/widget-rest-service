package com.lazko.board.widget.infrastructure;

import com.lazko.board.widget.domain.Widget;
import com.lazko.board.widget.domain.WidgetRepository;
import com.lazko.board.widget.domain.exception.NotFoundEntityException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Repository
@Transactional
public class DatabaseWidgetRepository implements WidgetRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public DatabaseWidgetRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Long generateId() {
        Query nativeQuery = entityManager.createNativeQuery("SELECT WIDGETS_SEQ.nextval");
        var id = nativeQuery.getSingleResult();
        return ((BigInteger)id).longValue();
    }

    @Override
    public Integer getMaxZ() {
        Query nativeQuery = entityManager.createNativeQuery("SELECT MAX(w.z) from WIDGETS w");
        Object maxZ = nativeQuery.getSingleResult();
        return maxZ == null ? 0: (Integer) maxZ;
    }

    @Override
    public boolean isEmptyZIndex(Integer z) {
        Query nativeQuery = entityManager.createNativeQuery("SELECT count(w.z) from WIDGETS w WHERE w.z = ?")
                .setParameter(1, z);
        var count = (BigInteger) nativeQuery.getSingleResult();
        long l = count.longValue();
        return l == 0;
    }

    @Override
    public List<Widget> getTailZ(Integer z) {
        Query query = entityManager.createNativeQuery(
            "SELECT w.ID, w.X, w.Y, w.Z, w.WIDTH, w.HEIGHT, w.UPDATEDAT FROM WIDGETS w WHERE w.Z >= ? ", Widget.class
        ).setParameter(1, z);
        return query.getResultList();
    }

    @Override
    public Page<Widget> findAll(Pageable pageable) {
        Query query = entityManager.createNativeQuery(
            "SELECT w.ID, w.X, w.Y, w.Z, w.WIDTH, w.HEIGHT, w.UPDATEDAT " +
               "FROM WIDGETS w ORDER BY w.Z ASC LIMIT ? OFFSET ?", Widget.class
        ).setParameter(1, pageable.getPageSize())
                .setParameter(2, pageable.getOffset());

        Query countQuery = entityManager.createNativeQuery("SELECT count(*) FROM WIDGETS");
        List<Widget> resultList = query.getResultList();
        var countValue = (BigInteger) countQuery.getSingleResult();
        return new PageImpl(resultList, pageable, countValue.longValue());
    }

    @Override
    public Widget getById(Long id) throws NotFoundEntityException {
        Query query = entityManager.createNativeQuery(
            "SELECT w.ID, w.X, w.Y, w.Z, w.WIDTH, w.HEIGHT, w.UPDATEDAT FROM widgets w WHERE w.id = ?", Widget.class
        ).setParameter(1, id);
        var widgets = query.getResultList();
        if (widgets.isEmpty())
            throw new NotFoundEntityException(id);
        return (Widget) widgets.get(0);
    }

    @Override
    public Widget save(Widget entity) {
        entityManager.createNativeQuery(
            "MERGE INTO widgets(id, x, y, z, width, height, updatedAt) KEY (id) VALUES (?,?,?,?,?,?,?)", Widget.class
        ).setParameter(1, entity.getId())
                .setParameter(2, entity.getX())
                .setParameter(3, entity.getY())
                .setParameter(4, entity.getZ())
                .setParameter(5, entity.getWidth())
                .setParameter(6, entity.getHeight())
                .setParameter(7, entity.getUpdatedAt())
                .executeUpdate();
        return entity;
    }

    @Override
    public Iterable<Widget> saveAll(List<Widget> entities) {
        for (var widget: entities)
            save(widget);
        return entities;
    }

    @Override
    public void deleteById(Long id) throws NotFoundEntityException {
        var widget = getById(id);
        entityManager.createNativeQuery("DELETE FROM widgets WHERE id = :id")
                .setParameter("id", widget.getId())
                .executeUpdate();
    }
}
