package ModandControl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping(path="/painter")
public class MainController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private BoardRepository boardRepository;


	    @GetMapping("/createBoard")
	    public @ResponseBody String createBoard(){
	        Board board1=new Board();
            board1.setMaxShapes(Constants.DEFAULTMAXSHAPES);
            User admin=new User();
            admin.setIp(request.getRemoteHost());
            board1.setAdmin(admin);
	        board1.startBoard();
            userRepository.save(admin);
	        boardRepository.save(board1);
	        return "ModandControl/Board" +board1.getId()+ " has been created, toggle to /board to add shapes";
        }

        @DeleteMapping("/board/deleteShape")
        public @ResponseBody  String deleteShape(@RequestBody String stringifiedObj) {
            JsonNode jsonObject = convertToJSONObj(stringifiedObj);
            if (jsonObject != null) {
                String ip=request.getRemoteHost();
                User user=userRepository.findUserByIp(ip);
                Shape shapetoRemove = createProperShape(jsonObject,true);
                Optional<Board> getBoard = boardRepository.findById(1);
                if (getBoard.isPresent()) {
                    Board myboard = getBoard.get();
                    if (user!=null && user.getIp().equals(myboard.getAdmin().getIp()) && myboard.removeShape(shapetoRemove, user)) {
                        boardRepository.save(myboard);
                    }
                }
            }
            return "";
        }


        @PostMapping("/board/boardAddShape")
        public @ResponseBody  String addShape(@RequestBody String str) {
            JsonNode jsonObject = convertToJSONObj(str);
            if (jsonObject != null) {
                String ip=request.getRemoteHost();
                User user=userRepository.findUserByIp(ip);
                Shape shapetoAdd = createProperShape(jsonObject,false);
                Optional<Board> getBoard = boardRepository.findById(1);
                if (getBoard.isPresent()) {
                    Board myboard = getBoard.get();
                    if (user!=null && myboard.addShape(shapetoAdd,user)) {
                        boardRepository.save(myboard);
                    }

                }
            }
            return "";
        }

        @GetMapping("/board/getShapes")
        public @ResponseBody String getCurrentShapes(){
	        Optional<Board> getboard=boardRepository.findById(1);
	        if(getboard.isPresent()){
	            getboard.get().resetAccordingTheDay();
	            return getboard.get().stringfyShapes();
            }
	        else
                return "";
        }

        @GetMapping("/board")
        public @ResponseBody ModelAndView showBoard(){
	        Optional<Board> getboard=boardRepository.findById(1);
	        String ip=request.getRemoteHost();
	        User user=userRepository.findUserByIp(ip);
            ModelAndView mdl=new ModelAndView("board");
	        if(getboard.isPresent()){
                Board myboard=getboard.get();
                if(user==null){
                    User newUser=new User();
                    newUser.setIp(ip);
                    myboard.addUser(newUser);
                    userRepository.save(newUser);
                }else if(!myboard.containsUser(user)){
                    myboard.addUser(user);
                }
                    getboard.get().resetAccordingTheDay();
                    boardRepository.save(myboard);
	                mdl.addObject("board",myboard);
	                return mdl;
            }
	        else
                return mdl;
        }



		private JsonNode convertToJSONObj(String toConvert){
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = null;
            try {
                jsonObject = mapper.readTree(toConvert);
            } catch (IOException e) {
                System.out.println("Couldn't parse str");
            }
            return jsonObject;
        }
        private Shape createProperShape(JsonNode jsonObject,Boolean toDeletion){
            Shape shapetoAdd = null;
            String name = jsonObject.get("name").asText();
            if (name.equals("Triangle")) {
                shapetoAdd = new Triangle();
                ((Triangle) shapetoAdd).setSize(jsonObject.get("size").asInt());
            } else if (name.equals("Square")) {
                shapetoAdd = new Square();
                ((Square) shapetoAdd).setSize(jsonObject.get("size").asInt());
            } else if (name.equals("Circle")) {
                shapetoAdd = new Circle();
                ((Circle) shapetoAdd).setRadius(jsonObject.get("radius").asInt());
            }
            if(shapetoAdd!=null) {
                shapetoAdd.setX(jsonObject.get("x").asInt());
                shapetoAdd.setY(jsonObject.get("y").asInt());
            }
            if(toDeletion)
                shapetoAdd.setId(jsonObject.get("id").asInt());
            return shapetoAdd;
        }
	}
