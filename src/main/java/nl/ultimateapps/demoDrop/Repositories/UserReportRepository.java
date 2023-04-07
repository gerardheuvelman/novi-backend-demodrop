package nl.ultimateapps.demoDrop.Repositories;

import nl.ultimateapps.demoDrop.Models.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    public Iterable<UserReport> findAllByOrderByCreatedDateDesc(); // UserReports sorted by date descending

}
