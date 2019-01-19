package com.leo.dao;

import com.leo.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    //List<Ticket> findbyOwner(Long owner);

    Ticket findOneByTicketNum(Long ticketNum);

    @Override
    @Modifying(clearAutomatically = true)
    Ticket save(Ticket ticket);

    @Modifying  // 多服务实例的情况下可以加(clearAutomatically = true)去锁数据
    @Query("update ticket set lockUser = ?1 where lockUser is null and ticketNum = ?2")
    int lockTicket(Long customerId, Long ticketNum);

    @Modifying
    @Query("update ticket set owner = ?1, lockUser =null where lockUser =?1 and ticketNum = ?2")
    int moveIicket(Long customerId, Long ticketNum);
}
