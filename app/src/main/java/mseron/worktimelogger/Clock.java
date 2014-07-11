package mseron.worktimelogger;


import java.util.Date;

/**
 * @author Martial Séron <martial.seron@gmail.com>
 */
public class Clock {
    public static final String TABLE_NAME           = "clocks";
    public static final String COL_ID               = "_ID";
    public static final String COL_NAME             = "name";
    public static final String COL_START_DATE       = "start_date";
    public static final String COL_END_DATE         = "end_date";
    public static final String COL_EVENT_TYPE_ID    = "event_type_id";

    private long     _id;
    private String  _name;
    private Date    _startDate;
    private Date    _endDate;
    private int     _eventTypeId;

    public Clock(long _id, String _name, Date _startDate, Date _endDate, int _eventTypeId) {
        this._id            = _id;
        this._name          = _name;
        this._startDate     = _startDate;
        this._endDate       = _endDate;
        this._eventTypeId   = _eventTypeId;
    }

    public Clock(String _name, int _eventTypeId) {
        this._name          = _name;
        this._eventTypeId   = _eventTypeId;

        this._startDate     = new Date();
        this._endDate       = null;
    }

    public long get_id() {
        return _id;
    }

    private void set_id(long _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public Date get_startDate() {
        return _startDate;
    }

    public void set_startDate(Date _startDate) {
        this._startDate = _startDate;
    }

    public Date get_endDate() {
        return _endDate;
    }

    public void set_endDate(Date _endDate) {
        this._endDate = _endDate;
    }

    public int get_eventTypeId() {
        return _eventTypeId;
    }

    public void set_eventTypeId(int _eventTypeId) {
        this._eventTypeId = _eventTypeId;
    }
}

