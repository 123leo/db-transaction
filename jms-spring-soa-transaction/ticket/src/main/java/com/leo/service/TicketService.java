package com.leo.service;

import com.leo.dto.OrderDTO;
import com.leo.dao.TicketRepository;
import com.leo.domain.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jiangpeng on 2018/10/9.
 */
@Service
public class TicketService {
    private static final Logger LOG = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = "order:new", containerFactory = "msgFactory")
    public void handleTicketLock(OrderDTO dto) {
        System.out.println("=====================");
        System.out.println(dto.toString());
        LOG.info("====> get new order for ticket lock:{}", dto);
        int lockCount = ticketRepository.lockTicket(dto.getCustomerId(), dto.getTicketNum());
        if (lockCount == 1) {
            dto.setStatus("TICKET_LOCKED");
            jmsTemplate.convertAndSend("order:locked", dto);
        } else {

        }
    }

    @Transactional
    @JmsListener(destination = "order:ticket_move", containerFactory = "msgFactory")
    public void handleTicketMove(OrderDTO dto) {
        LOG.info("get new order for ticket move:{}", dto);
        int count = ticketRepository.moveIicket(dto.getCustomerId(), dto.getTicketNum());

        if (count == 0) {
            LOG.warn("ticket already moved:{}", dto);
        }
        dto.setStatus("TICKED_MOVED");
        jmsTemplate.convertAndSend("order:finish", dto);
    }

    @Transactional
    public Ticket ticketLock(OrderDTO dto) {
        Ticket ticket = ticketRepository.findOneByTicketNum(dto.getTicketNum());
        ticket.setLockUser(dto.getCustomerId());
        ticket = ticketRepository.save(ticket);
        try {
            Thread.sleep(10 * 1000);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return ticket;
    }

    @Transactional
    public int ticketLock2(OrderDTO dto) {
        int lockCount = ticketRepository.lockTicket(dto.getCustomerId(), dto.getTicketNum());
        LOG.info("update ticket lock count:{}", lockCount);
        try {
            Thread.sleep(10 * 1000);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return lockCount;
    }
}
