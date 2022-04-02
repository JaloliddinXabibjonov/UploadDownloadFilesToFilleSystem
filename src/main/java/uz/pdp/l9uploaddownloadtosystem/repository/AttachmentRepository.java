package uz.pdp.l9uploaddownloadtosystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.l9uploaddownloadtosystem.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {

}
