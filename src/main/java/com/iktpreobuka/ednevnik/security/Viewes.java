package com.iktpreobuka.ednevnik.security;

public class Viewes {
	
	
	public static class PrivateView{};
	public static class StudentView extends PrivateView{};
	public static class ParentView extends StudentView{};
	public static class TeacherView extends ParentView{};
	public static class AdminView extends TeacherView{};
	
}
