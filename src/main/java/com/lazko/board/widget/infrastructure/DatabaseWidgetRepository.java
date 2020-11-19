package com.lazko.board.widget.infrastructure;

import com.lazko.board.widget.domain.ScreenArea;
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
        Query nativeQuery = entityManager.createNativeQuery("SELECT MAX(w.z) from widgets w");
        Object maxZ = nativeQuery.getSingleResult();
        return maxZ == null ? 0: (Integer) maxZ;
    }

    @Override
    public boolean isEmptyZIndex(Integer z) {
        Query nativeQuery = entityManager.createNativeQuery("SELECT count(w.z) from widgets w WHERE w.z = ?")
                .setParameter(1, z);
        var count = (BigInteger) nativeQuery.getSingleResult();
        long l = count.longValue();
        return l == 0;
    }

    @Override
    public List<Widget> getTailZ(Integer z) {
        Query query = entityManager.createNativeQuery(
            "SELECT w.id, w.x, w.y, w.z, w.width, w.height, w.updatedat FROM widgets w WHERE w.Z >= ? ", Widget.class
        ).setParameter(1, z);
        return query.getResultList();
    }

    @Override
    public Page<Widget> findAll(Pageable pageable, ScreenArea screenArea) {
        Query countQuery = entityManager.createNativeQuery("SELECT count(*) FROM widgets");
        var countValue = (BigInteger) countQuery.getSingleResult();

        if (!screenArea.isEmpty()){
            Query query = entityManager.createNativeQuery(
                "SELECT w.id, w.x, w.y, w.z, w.width, w.height, w.updatedat " +
                        "FROM widgets w " +
                        "WHERE w.x >= :x0 AND w.y <= :y0 AND w.x + w.width <= :x1 AND w.y + w.height <= :y1 " +
                        "ORDER BY w.z ASC LIMIT :lim OFFSET :off", Widget.class
            ).setParameter("x0", screenArea.x)
                    .setParameter("y0", screenArea.y)
                    .setParameter("x1", screenArea.x + screenArea.width)
                    .setParameter("y1", screenArea.y + screenArea.height)
                    .setParameter("lim", pageable.getPageSize())
                    .setParameter("off", pageable.getOffset());
            List<Widget> resultList = query.getResultList();
            return new PageImpl(resultList, pageable, countValue.longValue());
        }

        Query query = entityManager.createNativeQuery(
            "SELECT w.id, w.x, w.y, w.z, w.width, w.height, w.updatedat " +
               "FROM widgets w ORDER BY w.z ASC LIMIT ? OFFSET ?", Widget.class
        ).setParameter(1, pageable.getPageSize())
                .setParameter(2, pageable.getOffset());

        List<Widget> resultList = query.getResultList();
        return new PageImpl(resultList, pageable, countValue.longValue());
    }

    @Override
    public Widget getById(Long id) throws NotFoundEntityException {
        Query query = entityManager.createNativeQuery(
            "SELECT w.id, w.x, w.y, w.z, w.width, w.height, w.updatedat FROM widgets w WHERE w.id = ?", Widget.class
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
