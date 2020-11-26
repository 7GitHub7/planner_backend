package pl.programowaniezespolowe.planner.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.programowaniezespolowe.planner.user.User;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
}
