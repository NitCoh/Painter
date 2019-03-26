package ModandControl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/painter") // This means URL's start with /demo (after Application path)
public class MainController {
	@Autowired // This means to get the bean called userRepository
	// Which is auto-generated by Spring, we will use it to handle the data
	private UserRepository userRepository;
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private BoardRepository boardRepository;


	    @GetMapping("/createBoard")
	    public @ResponseBody String createBoard(){
	        Board board1=new Board();
            board1.setMaxShapes(Constants.DEFAULTMAXSHAPES);
	        board1.startBoard();
	        boardRepository.save(board1);
	        return "ModandControl/Board" +board1.getId()+ " has been created, toggle to /board to add shapes";
        }


        @GetMapping("/board")
        public @ResponseBody String showBoard(){
	        Optional<Board> getboard=boardRepository.findById(1);
            Model mdl=new ModelAndView("/painter/board");
            String str;
	        if(getboard.isPresent()){
	            Board myboard=getboard.get();
	            ObjectWriter ow= new ObjectMapper().writer().withDefaultPrettyPrinter();
	            try {
                    return ow.writeValueAsString(myboard);
                }catch(JsonProcessingException e){
	                str="Problem with JSON";
	                mdl.addAttribute("str",str);
	                return "board";
                }
            }
	        str="Not Present";
	        mdl.addAttribute("str",str);
	        return "board";
        }

        //TODO: Add inside the RequestParamter , x , y , size and shape and link it to the HTML page with ${}
	    @GetMapping(path = "/boardAddShape")
	    public @ResponseBody String addShape(@RequestParam Integer x,@RequestParam Integer y,@RequestParam Integer size,@RequestParam String shape) {
            Shape shapetoAdd = null;
            if (shape.equals("triangle")) {
                shapetoAdd = new Triangle();
                ((Triangle) shapetoAdd).setSize(size);
            } else if (shape.equals("square")) {
                shapetoAdd = new Square();
                ((Square) shapetoAdd).setSize(size);
            } else if (shape.equals("circle")) {
                shapetoAdd = new Circle();
                ((Circle) shapetoAdd).setRadius(size);
            }
            if (shape != null) {
                shapetoAdd.setCenterX(x);
                shapetoAdd.setCenterY(y);

                Optional<Board> getBoard = boardRepository.findById(1);
                if (getBoard.isPresent()) {
                    Board myboard = getBoard.get();
                    if(myboard.addShape(shapetoAdd)) {
                        boardRepository.save(myboard);
                        return "Adding the shape succeded";
                    }
                    else return "Couldn't add shape";
                }
                else
                    return "Failed to Add - Could'nt reach the board in the Database";
            }
            return "Wrong passing paramaters in POST request";
        }




		@GetMapping(path = "/add") // Map ONLY GET Requests
		public @ResponseBody String addNewUser (@RequestParam String name){
			// @ResponseBody means the returned String is the response, not a view name
			// @RequestParam means it is a parameter from the GET or POST request

			User n = new User();
			n.setName(name);
			n.setIp(request.getRemoteHost());
			System.out.println(request.getRemoteHost());
			userRepository.save(n);
			return (name + " Saved " + request.getRemoteHost());
		}

		@GetMapping(path = "/all")
		public @ResponseBody Iterable<User> getAllUsers () {
			// This returns a JSON or XML with the users
			return userRepository.findAll();
		}


	}