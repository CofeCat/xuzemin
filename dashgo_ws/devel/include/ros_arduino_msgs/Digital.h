// Generated by gencpp from file ros_arduino_msgs/Digital.msg
// DO NOT EDIT!


#ifndef ROS_ARDUINO_MSGS_MESSAGE_DIGITAL_H
#define ROS_ARDUINO_MSGS_MESSAGE_DIGITAL_H


#include <string>
#include <vector>
#include <map>

#include <ros/types.h>
#include <ros/serialization.h>
#include <ros/builtin_message_traits.h>
#include <ros/message_operations.h>

#include <std_msgs/Header.h>

namespace ros_arduino_msgs
{
template <class ContainerAllocator>
struct Digital_
{
  typedef Digital_<ContainerAllocator> Type;

  Digital_()
    : header()
    , value(0)  {
    }
  Digital_(const ContainerAllocator& _alloc)
    : header(_alloc)
    , value(0)  {
  (void)_alloc;
    }



   typedef  ::std_msgs::Header_<ContainerAllocator>  _header_type;
  _header_type header;

   typedef uint8_t _value_type;
  _value_type value;




  typedef boost::shared_ptr< ::ros_arduino_msgs::Digital_<ContainerAllocator> > Ptr;
  typedef boost::shared_ptr< ::ros_arduino_msgs::Digital_<ContainerAllocator> const> ConstPtr;

}; // struct Digital_

typedef ::ros_arduino_msgs::Digital_<std::allocator<void> > Digital;

typedef boost::shared_ptr< ::ros_arduino_msgs::Digital > DigitalPtr;
typedef boost::shared_ptr< ::ros_arduino_msgs::Digital const> DigitalConstPtr;

// constants requiring out of line definition



template<typename ContainerAllocator>
std::ostream& operator<<(std::ostream& s, const ::ros_arduino_msgs::Digital_<ContainerAllocator> & v)
{
ros::message_operations::Printer< ::ros_arduino_msgs::Digital_<ContainerAllocator> >::stream(s, "", v);
return s;
}

} // namespace ros_arduino_msgs

namespace ros
{
namespace message_traits
{



// BOOLTRAITS {'IsFixedSize': False, 'IsMessage': True, 'HasHeader': True}
// {'std_msgs': ['/opt/ros/indigo/share/std_msgs/cmake/../msg'], 'ros_arduino_msgs': ['/home/pi/dashgo_ws/src/ros_arduino_bridge/ros_arduino_msgs/msg']}

// !!!!!!!!!!! ['__class__', '__delattr__', '__dict__', '__doc__', '__eq__', '__format__', '__getattribute__', '__hash__', '__init__', '__module__', '__ne__', '__new__', '__reduce__', '__reduce_ex__', '__repr__', '__setattr__', '__sizeof__', '__str__', '__subclasshook__', '__weakref__', '_parsed_fields', 'constants', 'fields', 'full_name', 'has_header', 'header_present', 'names', 'package', 'parsed_fields', 'short_name', 'text', 'types']




template <class ContainerAllocator>
struct IsFixedSize< ::ros_arduino_msgs::Digital_<ContainerAllocator> >
  : FalseType
  { };

template <class ContainerAllocator>
struct IsFixedSize< ::ros_arduino_msgs::Digital_<ContainerAllocator> const>
  : FalseType
  { };

template <class ContainerAllocator>
struct IsMessage< ::ros_arduino_msgs::Digital_<ContainerAllocator> >
  : TrueType
  { };

template <class ContainerAllocator>
struct IsMessage< ::ros_arduino_msgs::Digital_<ContainerAllocator> const>
  : TrueType
  { };

template <class ContainerAllocator>
struct HasHeader< ::ros_arduino_msgs::Digital_<ContainerAllocator> >
  : TrueType
  { };

template <class ContainerAllocator>
struct HasHeader< ::ros_arduino_msgs::Digital_<ContainerAllocator> const>
  : TrueType
  { };


template<class ContainerAllocator>
struct MD5Sum< ::ros_arduino_msgs::Digital_<ContainerAllocator> >
{
  static const char* value()
  {
    return "90539346f3c3c8fc47f159ab9a6ff208";
  }

  static const char* value(const ::ros_arduino_msgs::Digital_<ContainerAllocator>&) { return value(); }
  static const uint64_t static_value1 = 0x90539346f3c3c8fcULL;
  static const uint64_t static_value2 = 0x47f159ab9a6ff208ULL;
};

template<class ContainerAllocator>
struct DataType< ::ros_arduino_msgs::Digital_<ContainerAllocator> >
{
  static const char* value()
  {
    return "ros_arduino_msgs/Digital";
  }

  static const char* value(const ::ros_arduino_msgs::Digital_<ContainerAllocator>&) { return value(); }
};

template<class ContainerAllocator>
struct Definition< ::ros_arduino_msgs::Digital_<ContainerAllocator> >
{
  static const char* value()
  {
    return "# Reading on a digital pin\n\
Header header\n\
uint8 value\n\
\n\
\n\
================================================================================\n\
MSG: std_msgs/Header\n\
# Standard metadata for higher-level stamped data types.\n\
# This is generally used to communicate timestamped data \n\
# in a particular coordinate frame.\n\
# \n\
# sequence ID: consecutively increasing ID \n\
uint32 seq\n\
#Two-integer timestamp that is expressed as:\n\
# * stamp.sec: seconds (stamp_secs) since epoch (in Python the variable is called 'secs')\n\
# * stamp.nsec: nanoseconds since stamp_secs (in Python the variable is called 'nsecs')\n\
# time-handling sugar is provided by the client library\n\
time stamp\n\
#Frame this data is associated with\n\
# 0: no frame\n\
# 1: global frame\n\
string frame_id\n\
";
  }

  static const char* value(const ::ros_arduino_msgs::Digital_<ContainerAllocator>&) { return value(); }
};

} // namespace message_traits
} // namespace ros

namespace ros
{
namespace serialization
{

  template<class ContainerAllocator> struct Serializer< ::ros_arduino_msgs::Digital_<ContainerAllocator> >
  {
    template<typename Stream, typename T> inline static void allInOne(Stream& stream, T m)
    {
      stream.next(m.header);
      stream.next(m.value);
    }

    ROS_DECLARE_ALLINONE_SERIALIZER;
  }; // struct Digital_

} // namespace serialization
} // namespace ros

namespace ros
{
namespace message_operations
{

template<class ContainerAllocator>
struct Printer< ::ros_arduino_msgs::Digital_<ContainerAllocator> >
{
  template<typename Stream> static void stream(Stream& s, const std::string& indent, const ::ros_arduino_msgs::Digital_<ContainerAllocator>& v)
  {
    s << indent << "header: ";
    s << std::endl;
    Printer< ::std_msgs::Header_<ContainerAllocator> >::stream(s, indent + "  ", v.header);
    s << indent << "value: ";
    Printer<uint8_t>::stream(s, indent + "  ", v.value);
  }
};

} // namespace message_operations
} // namespace ros

#endif // ROS_ARDUINO_MSGS_MESSAGE_DIGITAL_H
