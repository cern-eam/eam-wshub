package ch.cern.cmms.wshub.rest.tools;

import ch.cern.eam.wshub.core.tools.InforException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//TODO @JsonInclude(JsonInclude.Include.NON_NULL)
public class EAMResponse<T> {

	private T data;

	private List<Error> errors;

	private EAMResponse() {
	}

	private EAMResponse(T data, List<Error> errors) {
		this.data = data;
		this.errors = errors;
	}

	public static<T> EAMResponse fromData(T data) {
		return new EAMResponse(data, new ArrayList<>());
	}

	public static EAMResponse fromException(Exception exception) {
		return new EAMResponse(null, buildErrorsList(exception));
	}

	public EAMResponse(List<Error> errors) {
		this.errors = errors;
	}


	/**
	 * Create Error list from an exception
	 *
	 * @param exception
	 *            The exception that will become a list of errors
	 * @return The list of errors
	 */
	private static List<Error> buildErrorsList(Exception exception) {
		if (exception instanceof InforException
			&& ((InforException)exception).getExceptionInfoList() != null) {
			InforException inforException = (InforException) exception;
			return Arrays.asList(inforException.getExceptionInfoList())
					.stream()
					.map(error -> new Error("", "", error.getMessage()))
					.collect(Collectors.toList());
		}
		else {
			// Just the message
			List<Error> errorList = new ArrayList<>();
			errorList.add(new Error("", "", exception.getMessage()));
			return errorList;
		}

	}

	//TODO @JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Error {

		private String code;

		private String title;

		private String detail;

		public Error(String code, String title) {
			this.code = code;
			this.title = title;
		}

		public Error(String code, String title, String detail) {
			this(code, title);
			this.detail = detail;
		}

		public String getCode() {
			return code;
		}

		public String getTitle() {
			return title;
		}

		public String getDetail() {
			return detail;
		}

		@Override
		public String toString() {
			return "Error [" + (code != null ? "code=" + code + ", " : "")
					+ (title != null ? "title=" + title + ", " : "") + (detail != null ? "detail=" + detail : "") + "]";
		}

	}

	public T getData() {
		return data;
	}

	public List<Error> getErrors() {
		return errors;
	}

	@Override
	public String toString() {
		return "EAMResponse [" + (data != null ? "data=" + data + ", " : "")
				+ (errors != null ? "errors=" + errors : "") + "]";
	}

}
